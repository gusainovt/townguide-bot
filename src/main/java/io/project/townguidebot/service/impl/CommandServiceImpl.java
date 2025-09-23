package io.project.townguidebot.service.impl;

import io.project.townguidebot.listener.TelegramBot;
import io.project.townguidebot.service.CommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

import static io.project.townguidebot.model.CommandType.*;
import static io.project.townguidebot.service.constants.TelegramText.*;

@Slf4j
@Service
public class CommandServiceImpl implements CommandService {
    @Override
    public void initCommands(TelegramBot bot) {
        log.info("Initialization bot menu");
        List<BotCommand> listOfCommands = new ArrayList<>(List.of(
                new BotCommand(START.getValue(), START_DESCRIPTION),
                new BotCommand(HELP.getValue(), HELP_DESCRIPTION),
                new BotCommand(STORY.getValue(), STORY_DESCRIPTION),
                new BotCommand(WEATHER.getValue(), WEATHER_DESCRIPTION),
                new BotCommand(SELECT.getValue(), SELECT_DESCRIPTION),
                new BotCommand(PLACE.getValue(), PLACE_DESCRIPTION)
        ));
        try {
            bot.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));

        } catch (TelegramApiException e) {
            log.error("Error commands initialization: {}", e.getMessage());
        }
    }
}
