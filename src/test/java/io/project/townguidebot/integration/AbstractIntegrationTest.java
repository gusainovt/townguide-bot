package io.project.townguidebot.integration;

import io.project.townguidebot.config.BotInitializer;
import io.project.townguidebot.listener.TelegramBot;
import io.project.townguidebot.service.CommandService;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.MountableFile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractIntegrationTest {

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
}
