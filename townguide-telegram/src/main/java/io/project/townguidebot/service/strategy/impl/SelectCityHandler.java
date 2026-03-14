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
public class SelectCityHandler implements CallbackSendMessageStrategy {

    private final ButtonCallback buttonCallback = ButtonCallback.SELECT_CITY;
    private final SendingService sendingService;

    @Override
    public ButtonCallback getButtonCallback() {
        return buttonCallback;
    }

    @Override
    public SendMessage handle(Message message) {
        return sendingService.selectCityCommandReceived(message.getChatId());
    }
}
