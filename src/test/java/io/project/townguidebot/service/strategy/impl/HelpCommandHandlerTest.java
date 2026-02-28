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
class HelpCommandHandlerTest {

    @Mock
    private SendingService sendingService;

    @Test
    void getCommandType_ShouldReturnHelp() {
        HelpCommandHandler handler = new HelpCommandHandler(sendingService);

        assertEquals(CommandType.HELP, handler.getCommandType());
    }

    @Test
    void handle_ShouldExecuteHelpText() throws Exception {
        HelpCommandHandler handler = new HelpCommandHandler(sendingService);
        TelegramLongPollingBot bot = org.mockito.Mockito.mock(TelegramLongPollingBot.class);
        SendMessage msg = new SendMessage();
        when(sendingService.sendHelpText(10L)).thenReturn(msg);

        handler.handle(bot, 10L);

        verify(sendingService).sendHelpText(10L);
        verify(bot).execute(msg);
    }
}

