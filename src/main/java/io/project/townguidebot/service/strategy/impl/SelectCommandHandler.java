package io.project.townguidebot.service.strategy.impl;

import io.project.townguidebot.model.CommandType;
import io.project.townguidebot.service.SendingService;
import io.project.townguidebot.service.strategy.CommandHandlerStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Component
@Slf4j
@RequiredArgsConstructor
public class SelectCommandHandler implements CommandHandlerStrategy {

    private final CommandType commandType = CommandType.SELECT;
    private final SendingService sendingService;

    @Override
    public CommandType getCommandType() {
        return commandType;
    }

    @Override
    public void handle(TelegramLongPollingBot bot, long chatId) throws TelegramApiException {
        log.info("Select city command handle for chat: {}", chatId);
        bot.execute(sendingService.selectCityCommandReceived(chatId));
    }
}
