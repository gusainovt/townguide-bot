package io.project.townguidebot.service.strategy.impl;

import io.project.townguidebot.model.enums.CommandType;
import io.project.townguidebot.service.SendingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StartCommandHandlerTest {

    @Mock
    private SendingService sendingService;

    @Test
    void getCommandType_ShouldReturnStart() {
        StartCommandHandler handler = new StartCommandHandler(sendingService);

        assertEquals(CommandType.START, handler.getCommandType());
    }

    @Test
    void handle_ShouldExecuteStartCommandReceived() throws Exception {
        StartCommandHandler handler = new StartCommandHandler(sendingService);
        TelegramLongPollingBot bot = org.mockito.Mockito.mock(TelegramLongPollingBot.class);
        SendPhoto msg = new SendPhoto();
        when(sendingService.startCommandReceived(10L)).thenReturn(msg);

        handler.handle(bot, 10L);

        verify(sendingService).startCommandReceived(10L);
        verify(bot).execute(msg);
    }
}

