package io.project.townguidebot.listener;

import io.project.townguidebot.config.BotConfig;
import io.project.townguidebot.exception.EmptyMessageException;
import io.project.townguidebot.model.enums.CommandType;
import io.project.townguidebot.model.enums.MenuType;
import io.project.townguidebot.service.CityService;
import io.project.townguidebot.service.CommandService;
import io.project.townguidebot.service.PlaceService;
import io.project.townguidebot.service.UserService;
import io.project.townguidebot.service.strategy.CommandHandlerStrategy;
import io.project.townguidebot.service.strategy.MenuStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TelegramBotTest {

    @Mock
    private BotConfig config;

    @Mock
    private UserService userService;

    @Mock
    private CityService cityService;

    @Mock
    private PlaceService placeService;

    @Mock
    private CommandService commandService;

    @Mock
    private CommandHandlerStrategy startStrategy;

    @Mock
    private CommandHandlerStrategy defaultStrategy;

    @Mock
    private MenuStrategy cityMenuStrategy;

    private TelegramBot bot;

    @BeforeEach
    void setUp() {
        when(startStrategy.getCommandType()).thenReturn(CommandType.START);
        when(defaultStrategy.getCommandType()).thenReturn(CommandType.DEFAULT);
        when(cityMenuStrategy.getMenuType()).thenReturn(MenuType.CITY);

        bot = new TelegramBot(
                config,
                userService,
                cityService,
                placeService,
                commandService,
                List.of(startStrategy, defaultStrategy),
                List.of(cityMenuStrategy)
        );
        bot.init();
    }

    @Test
    void init_ShouldRegisterStrategiesAndInitCommands() {
        verify(commandService).initCommands(bot);
    }

    @Test
    void getBotUsername_ShouldReturnConfigName() {
        when(config.getName()).thenReturn("bot");

        String result = bot.getBotUsername();

        assertEquals("bot", result);
        verify(config).getName();
    }

    @Test
    void getBotToken_ShouldReturnConfigToken() {
        when(config.getToken()).thenReturn("token");

        String result = bot.getBotToken();

        assertEquals("token", result);
        verify(config).getToken();
    }

    @Test
    void onUpdateReceived_WhenNoMessage_ShouldThrowEmptyMessageException() {
        Update update = org.mockito.Mockito.mock(Update.class);
        when(update.getMessage()).thenReturn(null);
        when(update.hasCallbackQuery()).thenReturn(false);

        EmptyMessageException ex = assertThrows(EmptyMessageException.class, () -> bot.onUpdateReceived(update));

        assertEquals("Message is empty or not found", ex.getMessage());
    }

    @Test
    void onUpdateReceived_WhenNotRegistered_ShouldRegisterUser() {
        Update update = org.mockito.Mockito.mock(Update.class);
        Message message = org.mockito.Mockito.mock(Message.class);
        Chat chat = org.mockito.Mockito.mock(Chat.class);
        when(update.getMessage()).thenReturn(message);
        when(update.hasCallbackQuery()).thenReturn(false);
        when(message.getChatId()).thenReturn(10L);
        when(message.getChat()).thenReturn(chat);

        when(userService.isRegisteredUser(10L)).thenReturn(false);

        bot.onUpdateReceived(update);

        verify(userService).registeredUser(10L, chat);
    }

    @Test
    void onUpdateReceived_WhenCommandMessage_ShouldHandleCommandStrategy() throws Exception {
        Update update = org.mockito.Mockito.mock(Update.class);
        Message message = org.mockito.Mockito.mock(Message.class);

        when(update.getMessage()).thenReturn(message);
        when(update.hasCallbackQuery()).thenReturn(false);
        when(update.hasMessage()).thenReturn(true);
        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn("/start");
        when(message.getChatId()).thenReturn(10L);

        when(userService.isRegisteredUser(10L)).thenReturn(true);

        bot.onUpdateReceived(update);

        verify(startStrategy).handle(bot, 10L);
        verify(defaultStrategy, never()).handle(any(), anyLong());
    }

    @Test
    void onUpdateReceived_WhenUnknownCommand_ShouldFallbackToDefaultStrategy() throws Exception {
        Update update = org.mockito.Mockito.mock(Update.class);
        Message message = org.mockito.Mockito.mock(Message.class);

        when(update.getMessage()).thenReturn(message);
        when(update.hasCallbackQuery()).thenReturn(false);
        when(update.hasMessage()).thenReturn(true);
        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn("/unknown");
        when(message.getChatId()).thenReturn(10L);

        when(userService.isRegisteredUser(10L)).thenReturn(true);

        bot.onUpdateReceived(update);

        verify(defaultStrategy).handle(bot, 10L);
        verify(startStrategy, never()).handle(any(), anyLong());
    }

    @Test
    void onUpdateReceived_WhenNotRegisteredAndCommandMessage_ShouldRegisterThenHandleCommand() throws Exception {
        Update update = org.mockito.Mockito.mock(Update.class);
        Message message = org.mockito.Mockito.mock(Message.class);
        Chat chat = org.mockito.Mockito.mock(Chat.class);

        when(update.getMessage()).thenReturn(message);
        when(update.hasCallbackQuery()).thenReturn(false);
        when(update.hasMessage()).thenReturn(true);
        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn("/start");
        when(message.getChatId()).thenReturn(10L);
        when(message.getChat()).thenReturn(chat);

        when(userService.isRegisteredUser(10L)).thenReturn(false);

        bot.onUpdateReceived(update);

        verify(userService).registeredUser(10L, chat);
        verify(startStrategy).handle(bot, 10L);
    }

    @Test
    void onUpdateReceived_WhenCallbackQuery_ShouldSelectCityPlaceAndHandleMenu() throws Exception {
        Update update = org.mockito.Mockito.mock(Update.class);
        CallbackQuery callbackQuery = org.mockito.Mockito.mock(CallbackQuery.class);
        Message message = org.mockito.Mockito.mock(Message.class);

        when(update.getMessage()).thenReturn(null);
        when(update.hasCallbackQuery()).thenReturn(true);
        when(update.getCallbackQuery()).thenReturn(callbackQuery);
        when(callbackQuery.getData()).thenReturn("CITY:moscow");
        when(callbackQuery.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(10L);

        when(userService.isRegisteredUser(10L)).thenReturn(true);

        bot.onUpdateReceived(update);

        verify(cityService).selectedCity("CITY:moscow", 10L);
        verify(placeService).selectedPlace("CITY:moscow", 10L);
        verify(cityMenuStrategy).handle(bot, update);
        verify(startStrategy, never()).handle(any(), anyLong());
    }
}
