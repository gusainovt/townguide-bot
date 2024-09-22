package io.project.townguidebot.service.strategy;

import io.project.townguidebot.model.ButtonCallback;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface CallbackSendMessageStrategy {
    ButtonCallback getButtonCallback();
    SendMessage handle(Message message);
}
