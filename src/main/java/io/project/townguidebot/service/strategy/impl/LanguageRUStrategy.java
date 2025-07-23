package io.project.townguidebot.service.strategy.impl;

import io.project.townguidebot.model.ButtonCallback;
import io.project.townguidebot.service.SendingService;
import io.project.townguidebot.service.UserService;
import io.project.townguidebot.service.strategy.RegisterStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;

import static io.project.townguidebot.model.LanguageCode.RU;
import static io.project.townguidebot.service.constants.TelegramText.REGISTER_CONFIRMATION;

@RequiredArgsConstructor
@Component
public class LanguageRUStrategy implements RegisterStrategy {

    private final ButtonCallback buttonCallback = ButtonCallback.LANGUAGE_CODE_RU;
    private final UserService userService;
    private final SendingService sendingService;


    @Override
    public ButtonCallback getButtonCallback() {
        return buttonCallback;
    }

    @Override
    public EditMessageText handle(Message message) {
        long messageId = message.getMessageId();
        long chatId = message.getChatId();
        userService.registeredUser(message, RU);
        return sendingService.sendEditMessageText(REGISTER_CONFIRMATION, chatId, messageId);
    }
}
