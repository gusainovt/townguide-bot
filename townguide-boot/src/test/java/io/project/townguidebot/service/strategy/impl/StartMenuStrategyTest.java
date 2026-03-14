package io.project.townguidebot.service.strategy.impl;

import io.project.townguidebot.model.enums.MenuType;
import io.project.townguidebot.service.CallbackService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StartMenuStrategyTest {

    @Mock
    private CallbackService callbackService;

    @Test
    void getMenuType_ShouldReturnStart() {
        StartMenuStrategy strategy = new StartMenuStrategy(callbackService);

        assertEquals(MenuType.START, strategy.getMenuType());
    }

    @Test
    void handle_ShouldExecuteButtonStart() throws Exception {
        StartMenuStrategy strategy = new StartMenuStrategy(callbackService);
        TelegramLongPollingBot bot = org.mockito.Mockito.mock(TelegramLongPollingBot.class);
        Update update = org.mockito.Mockito.mock(Update.class);
        SendMessage sendMessage = new SendMessage();
        when(callbackService.buttonStart(update)).thenReturn(sendMessage);

        strategy.handle(bot, update);

        verify(callbackService).buttonStart(update);
        verify(bot).execute(sendMessage);
    }
}

