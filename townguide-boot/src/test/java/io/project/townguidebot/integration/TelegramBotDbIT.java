package io.project.townguidebot.integration;

import io.project.townguidebot.config.BotInitializer;
import io.project.townguidebot.listener.TelegramBot;
import io.project.townguidebot.model.User;
import io.project.townguidebot.model.enums.LanguageCode;
import io.project.townguidebot.repository.UserRepository;
import io.project.townguidebot.service.CommandService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Testcontainers
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TelegramBotDbIT {

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

    @Autowired
    private TelegramBot telegramBot;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    CommandService commandService;

    @MockBean
    BotInitializer botInitializer;

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    void onUpdateReceived_WhenUserNotRegistered_ShouldCreateUserInDatabase() throws Exception {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);

        when(update.getMessage()).thenReturn(message);
        when(update.hasCallbackQuery()).thenReturn(false);
        when(update.hasMessage()).thenReturn(false);
        when(message.getChatId()).thenReturn(10L);
        when(message.getChat()).thenReturn(chat);

        when(chat.getFirstName()).thenReturn("Anna");
        when(chat.getLastName()).thenReturn("Ivanova");
        when(chat.getUserName()).thenReturn("ann");

        telegramBot.onUpdateReceived(update);

        User saved = userRepository.findById(10L).orElseThrow();
        assertEquals(10L, saved.getChatId());
        assertEquals("Anna", saved.getFirstName());
        assertEquals("Ivanova", saved.getLastName());
        assertEquals("ann", saved.getUserName());
        assertNotNull(saved.getRegisteredAt());
        assertEquals(LanguageCode.RU, saved.getLanguageCode());
    }
}
