package io.project.townguidebot.service.strategy;

import io.project.townguidebot.model.enums.MenuType;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface MenuStrategy {
    MenuType getMenuType();
    void handle(TelegramLongPollingBot bot, Update update) throws TelegramApiException;
}
