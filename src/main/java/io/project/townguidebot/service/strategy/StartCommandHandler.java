package io.project.townguidebot.service.strategy;

import io.project.townguidebot.model.CommandType;
import io.project.townguidebot.service.SendingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Component
@RequiredArgsConstructor
public class StartCommandHandler implements CommandHandlerStrategy {

    private final CommandType commandType = CommandType.START;

    private final SendingService sendingService;


    @Override
    public CommandType getCommandType() {
        return commandType;
    }

    @Override
    public void handle(TelegramLongPollingBot bot, long chatId) throws TelegramApiException {
        bot.execute(sendingService.startCommandReceived(chatId));
    }
}


