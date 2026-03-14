package io.project.townguidebot.integration;

import io.project.townguidebot.config.BotInitializer;
import io.project.townguidebot.listener.TelegramBot;
import io.project.townguidebot.security.dto.LoginRequest;
import io.project.townguidebot.security.dto.TokenResponse;
import io.project.townguidebot.security.model.AdminUser;
import io.project.townguidebot.security.repository.AdminUserRepository;
import io.project.townguidebot.service.CommandService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthSecurityIT {

    @Container
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("townguide")
            .withUsername("test")
            .withPassword("test");

    @Container
    static final GenericContainer<?> WIREMOCK = new GenericContainer<>("wiremock/wiremock:3.5.4")
            .withExposedPorts(8080)
            .withCopyFileToContainer(MountableFile.forClasspathResource("wiremock/mappings"), "/home/wiremock/mappings")
            .withCopyFileToContainer(MountableFile.forClasspathResource("wiremock/__files"), "/home/wiremock/__files");

    @MockBean
    CommandService commandService;

    @MockBean
    TelegramBot telegramBot;

    @MockBean
    BotInitializer botInitializer;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @DynamicPropertySource
    static void registerProps(DynamicPropertyRegistry registry) {
        if (!POSTGRES.isRunning()) {
            POSTGRES.start();
        }
        if (!WIREMOCK.isRunning()) {
            WIREMOCK.start();
        }

        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);

        registry.add("weather-forecast-service.api-key", () -> "test-api-key");
        registry.add("weather-forecast-service.url", () ->
                "http://" + WIREMOCK.getHost() + ":" + WIREMOCK.getMappedPort(8080)
                        + "/weather?city={city}&apiKey={apiKey}");
    }

    @BeforeEach
    void setUp() {
        adminUserRepository.deleteAll();
        AdminUser admin = new AdminUser();
        admin.setUsername("admin");
        admin.setPasswordHash(passwordEncoder.encode("pass"));
        admin.setRole(AdminUser.Role.ADMIN);
        adminUserRepository.save(admin);
    }

    @Test
    void protectedEndpoint_WithoutToken_ShouldReturn403() {
        ResponseEntity<String> resp = restTemplate.getForEntity("/api/v1/city", String.class);
        assertEquals(403, resp.getStatusCode().value());
    }

    @Test
    void protectedEndpoint_WithValidToken_ShouldReturn200() {
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("pass");

        ResponseEntity<TokenResponse> loginResp = restTemplate.postForEntity("/auth/login", request, TokenResponse.class);
        assertEquals(200, loginResp.getStatusCode().value());
        assertNotNull(loginResp.getBody());
        assertNotNull(loginResp.getBody().token());

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(loginResp.getBody().token());

        ResponseEntity<String> resp = restTemplate.exchange(
                "/api/v1/city",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
        );

        assertEquals(200, resp.getStatusCode().value());
    }
}

