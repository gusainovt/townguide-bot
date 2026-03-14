package io.project.townguidebot.service.strategy.impl;

import io.project.townguidebot.model.enums.CommandType;
import io.project.townguidebot.service.SendingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlaceCommandHandlerTest {

    @Mock
    private SendingService sendingService;

    @Test
    void getCommandType_ShouldReturnPlace() {
        PlaceCommandHandler handler = new PlaceCommandHandler(sendingService);

        assertEquals(CommandType.PLACE, handler.getCommandType());
    }

    @Test
    void handle_ShouldExecuteRandomPlace() throws Exception {
        PlaceCommandHandler handler = new PlaceCommandHandler(sendingService);
        TelegramLongPollingBot bot = org.mockito.Mockito.mock(TelegramLongPollingBot.class);
        SendMessage msg = new SendMessage();
        when(sendingService.sendRandomPlace(10L)).thenReturn(msg);

        handler.handle(bot, 10L);

        verify(sendingService).sendRandomPlace(10L);
        verify(bot).execute(msg);
    }
}

