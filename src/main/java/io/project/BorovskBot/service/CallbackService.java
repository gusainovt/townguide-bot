package io.project.BorovskBot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface CallbackService {
    EditMessageText buttonRegister(Update update, String callbackData);
    SendMessage buttonStart(Update update, String callbackData);
}
