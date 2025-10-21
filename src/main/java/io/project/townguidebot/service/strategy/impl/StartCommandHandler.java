package io.project.townguidebot.service.strategy.impl;

import io.project.townguidebot.model.enums.CommandType;
import io.project.townguidebot.service.SendingService;
import io.project.townguidebot.service.strategy.CommandHandlerStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Component
@RequiredArgsConstructor
@Slf4j
public class StartCommandHandler implements CommandHandlerStrategy {

    private final CommandType commandType = CommandType.START;

    private final SendingService sendingService;


    @Override
    public CommandType getCommandType() {
        return commandType;
    }

    @Override
    public void handle(TelegramLongPollingBot bot, long chatId) throws TelegramApiException {
        log.info("Start command handle and remove city for chat: {}", chatId);
        bot.execute(sendingService.startCommandReceived(chatId));
    }
}


