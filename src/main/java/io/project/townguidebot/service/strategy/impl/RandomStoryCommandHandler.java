package io.project.townguidebot.service.strategy.impl;

import io.project.townguidebot.model.CommandType;
import io.project.townguidebot.service.SendingService;
import io.project.townguidebot.service.strategy.CommandHandlerStrategy;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Component
@AllArgsConstructor
public class RandomStoryCommandHandler implements CommandHandlerStrategy {

    private final CommandType commandType = CommandType.STORY;

    private final SendingService sendingService;


    @Override
    public CommandType getCommandType() {
        return commandType;
    }

    @Override
    public void handle(TelegramLongPollingBot bot, long chatId) throws TelegramApiException {
        bot.execute(sendingService.sendRandomStory(chatId));
    }




}
