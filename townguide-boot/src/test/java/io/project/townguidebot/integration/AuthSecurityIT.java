package io.project.townguidebot.integration;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.project.townguidebot.config.BotInitializer;
import io.project.townguidebot.listener.TelegramBot;
import io.project.townguidebot.security.dto.LoginRequest;
import io.project.townguidebot.security.dto.MessageResponse;
import io.project.townguidebot.security.dto.TokenResponse;
import io.project.townguidebot.security.model.AdminUser;
import io.project.townguidebot.security.repository.AdminUserRepository;
import io.project.townguidebot.service.CommandService;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
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
import org.springframework.http.HttpStatus;
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

    private static final String JWT_SECRET = "0123456789abcdef0123456789abcdef";
    private static final SecretKey SIGNING_KEY = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));

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
        admin.setLogin("admin");
        admin.setName("Иван");
        admin.setFullName("Иван Петров");
        admin.setPasswordHash(passwordEncoder.encode("pass"));
        admin.setRole(AdminUser.Role.ADMIN);
        adminUserRepository.save(admin);

        AdminUser user = new AdminUser();
        user.setUsername("user");
        user.setLogin("user");
        user.setName("Петр");
        user.setFullName("Петр Иванов");
        user.setPasswordHash(passwordEncoder.encode("pass"));
        user.setRole(AdminUser.Role.USER);
        adminUserRepository.save(user);
    }

    @Test
    void protectedEndpoint_WithoutToken_ShouldReturn401() {
        ResponseEntity<MessageResponse> response = restTemplate.getForEntity("/api/v1/city", MessageResponse.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Требуется авторизация", response.getBody().message());
    }

    @Test
    void protectedEndpoint_WithValidAdminToken_ShouldReturn200() {
        String accessToken = loginAndGetAccessToken("admin");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/v1/city",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void protectedEndpoint_WithBrokenToken_ShouldReturn401() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("broken.token");

        ResponseEntity<MessageResponse> response = restTemplate.exchange(
                "/api/v1/city",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                MessageResponse.class
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Access token недействителен или истек", response.getBody().message());
    }

    @Test
    void protectedEndpoint_WithExpiredToken_ShouldReturn401() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(expiredAccessToken("admin"));

        ResponseEntity<MessageResponse> response = restTemplate.exchange(
                "/api/v1/city",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                MessageResponse.class
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Access token недействителен или истек", response.getBody().message());
    }

    @Test
    void protectedEndpoint_WithValidTokenButInsufficientRole_ShouldReturn403() {
        String accessToken = loginAndGetAccessToken("user");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        ResponseEntity<MessageResponse> response = restTemplate.exchange(
                "/api/v1/city",
                HttpMethod.POST,
                new HttpEntity<>("{\"name\":\"Москва\",\"nameEng\":\"moscow\",\"description\":\"desc\",\"callback\":\"cb\"}", headers),
                MessageResponse.class
        );

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Недостаточно прав для создания города", response.getBody().message());
    }

    private String loginAndGetAccessToken(String username) {
        LoginRequest request = new LoginRequest();
        request.setUsername(username);
        request.setPassword("pass");

        ResponseEntity<TokenResponse> response = restTemplate.postForEntity("/auth/login", request, TokenResponse.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        return response.getBody().token();
    }

    private String expiredAccessToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .claim("tokenType", "ACCESS")
                .setIssuedAt(new Date(System.currentTimeMillis() - 10_000))
                .setExpiration(new Date(System.currentTimeMillis() - 1_000))
                .signWith(SIGNING_KEY)
                .compact();
    }
}
