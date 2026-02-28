package io.project.townguidebot.service.strategy.impl;

import io.project.townguidebot.model.enums.MenuType;
import io.project.townguidebot.service.CallbackService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlaceMenuStrategyTest {

    @Mock
    private CallbackService callbackService;

    @Test
    void getMenuType_ShouldReturnPlace() {
        PlaceMenuStrategy strategy = new PlaceMenuStrategy(callbackService);

        assertEquals(MenuType.PLACE, strategy.getMenuType());
    }

    @Test
    void handle_ShouldExecuteButtonPlace() throws Exception {
        PlaceMenuStrategy strategy = new PlaceMenuStrategy(callbackService);
        TelegramLongPollingBot bot = org.mockito.Mockito.mock(TelegramLongPollingBot.class);
        Update update = org.mockito.Mockito.mock(Update.class);
        SendPhoto sendPhoto = new SendPhoto();
        when(callbackService.buttonPlace(update)).thenReturn(sendPhoto);

        strategy.handle(bot, update);

        verify(callbackService).buttonPlace(update);
        verify(bot).execute(sendPhoto);
    }
}

