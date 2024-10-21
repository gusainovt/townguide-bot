package io.project.townguidebot.service.strategy.impl;

import io.project.townguidebot.model.enums.ButtonCallback;
import io.project.townguidebot.service.SendingService;
import io.project.townguidebot.service.strategy.CallbackSendMessageStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@Slf4j
@RequiredArgsConstructor
public class WeatherHandler implements CallbackSendMessageStrategy {

    private final ButtonCallback buttonCallback = ButtonCallback.WEATHER;
    private final SendingService sendingService;

    @Override
    public ButtonCallback getButtonCallback() {
        return buttonCallback;
    }

    @Override
    public SendMessage handle(Message message) {
        long chatId = message.getChatId();
        return sendingService.sendWeather(chatId);
    }
}
