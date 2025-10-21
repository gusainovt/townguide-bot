package io.project.townguidebot.service.strategy.impl;

import io.project.townguidebot.model.enums.MenuType;
import io.project.townguidebot.service.CallbackService;
import io.project.townguidebot.service.strategy.MenuStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static io.project.townguidebot.model.enums.MenuType.START;

@Component
@RequiredArgsConstructor
public class StartMenuStrategy implements MenuStrategy {

    private final MenuType menuType = START;

    private final CallbackService callbackService;

    @Override
    public MenuType getMenuType() {
        return menuType;
    }

    @Override
    public void handle(TelegramLongPollingBot bot, Update update) throws TelegramApiException {
        bot.execute(callbackService.buttonStart(update));
    }
}
