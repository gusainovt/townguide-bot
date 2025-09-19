package io.project.townguidebot.service.strategy.impl;

import io.project.townguidebot.model.MenuType;
import io.project.townguidebot.service.CallbackService;
import io.project.townguidebot.service.strategy.MenuStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@RequiredArgsConstructor
@Slf4j
public class CityMenuStrategy implements MenuStrategy {

    private final MenuType menuType = MenuType.CITY;
    private final CallbackService callbackService;

    @Override
    public MenuType getMenuType() {
        return menuType;
    }

    @Override
    public void handle(TelegramLongPollingBot bot, Update update, String cityName) throws TelegramApiException {
        log.info("Generating start menu for city: {}", cityName);
        bot.execute(callbackService.buttonStart(cityName, update));
    }
}
