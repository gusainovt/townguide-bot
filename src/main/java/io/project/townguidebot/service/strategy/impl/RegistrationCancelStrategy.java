package io.project.townguidebot.service.strategy.impl;

import io.project.townguidebot.model.ButtonCallback;
import io.project.townguidebot.service.SendingService;
import io.project.townguidebot.service.strategy.RegisterStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;

import static io.project.townguidebot.service.constants.TelegramText.REGISTER_CANCEL;

@Component
@RequiredArgsConstructor
public class RegistrationCancelStrategy implements RegisterStrategy {

    private final ButtonCallback buttonCallback = ButtonCallback.CANCEL;
    private final SendingService sendingService;

    @Override
    public ButtonCallback getButtonCallback() {
        return buttonCallback;
    }

    @Override
    public EditMessageText handle(Message message) {
        long messageId = message.getMessageId();
        long chatId = message.getChatId();
        return sendingService.sendEditMessageText(REGISTER_CANCEL, chatId, messageId);
    }
}
