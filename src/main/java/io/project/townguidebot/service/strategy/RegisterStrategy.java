package io.project.townguidebot.service.strategy;

import io.project.townguidebot.model.ButtonCallback;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface RegisterStrategy {
    ButtonCallback getButtonCallback();
    EditMessageText handle(Message message);
}
