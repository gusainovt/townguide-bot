package io.project.townguidebot.service.strategy;

import io.project.townguidebot.model.enums.CommandType;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public interface CommandHandlerStrategy {

    CommandType getCommandType();

    void handle(TelegramLongPollingBot bot, long chatId) throws TelegramApiException;
}
