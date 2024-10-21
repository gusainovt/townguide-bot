package io.project.townguidebot.service.strategy.impl;

import io.project.townguidebot.model.enums.ButtonCallback;
import io.project.townguidebot.service.SendingService;
import io.project.townguidebot.service.strategy.CallbackSendMessageStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@RequiredArgsConstructor
public class CityHandler implements CallbackSendMessageStrategy {

    private static final ButtonCallback BUTTON_CALLBACK = ButtonCallback.CITY;
    private final SendingService sendingService;

    @Override
    public ButtonCallback getButtonCallback() {
        return BUTTON_CALLBACK;
    }

    @Override
    public SendMessage handle(Message message) {
        return sendingService.cityMenuReceived(message.getChatId());
    }
}
