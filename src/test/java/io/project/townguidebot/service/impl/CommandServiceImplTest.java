package io.project.townguidebot.service.impl;

import io.project.townguidebot.listener.TelegramBot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

import static io.project.townguidebot.model.enums.CommandType.*;
import static io.project.townguidebot.service.constants.TelegramText.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommandServiceImplTest {

    @Mock
    private TelegramBot bot;

    private CommandServiceImpl commandService;

    @BeforeEach
    void setUp() {
        commandService = new CommandServiceImpl();
    }

    @Test
    void initCommands_ShouldExecuteSetMyCommandsWithExpectedCommands() throws TelegramApiException {
        // Arrange
        when(bot.execute(any(SetMyCommands.class))).thenReturn(true);

        // Act
        commandService.initCommands(bot);

        // Assert
        ArgumentCaptor<SetMyCommands> captor = ArgumentCaptor.forClass(SetMyCommands.class);
        verify(bot).execute(captor.capture());
        SetMyCommands setMyCommands = captor.getValue();

        assertNotNull(setMyCommands);
        assertInstanceOf(BotCommandScopeDefault.class, setMyCommands.getScope());
        assertNull(setMyCommands.getLanguageCode());

        List<BotCommand> commands = setMyCommands.getCommands();
        assertNotNull(commands);
        assertEquals(6, commands.size());

        assertEquals(START.getValue(), commands.get(0).getCommand());
        assertEquals(START_DESCRIPTION, commands.get(0).getDescription());

        assertEquals(HELP.getValue(), commands.get(1).getCommand());
        assertEquals(HELP_DESCRIPTION, commands.get(1).getDescription());

        assertEquals(STORY.getValue(), commands.get(2).getCommand());
        assertEquals(STORY_DESCRIPTION, commands.get(2).getDescription());

        assertEquals(WEATHER.getValue(), commands.get(3).getCommand());
        assertEquals(WEATHER_DESCRIPTION, commands.get(3).getDescription());

        assertEquals(SELECT.getValue(), commands.get(4).getCommand());
        assertEquals(SELECT_DESCRIPTION, commands.get(4).getDescription());

        assertEquals(PLACE.getValue(), commands.get(5).getCommand());
        assertEquals(PLACE_DESCRIPTION, commands.get(5).getDescription());

        verifyNoMoreInteractions(bot);
    }

    @Test
    void initCommands_WhenTelegramApiException_ShouldNotThrow() throws TelegramApiException {
        // Arrange
        when(bot.execute(any(SetMyCommands.class))).thenThrow(new TelegramApiException("Failure"));

        // Act & Assert
        assertDoesNotThrow(() -> commandService.initCommands(bot));
        verify(bot).execute(any(SetMyCommands.class));
    }
}

