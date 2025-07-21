package io.project.townguidebot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;

public interface CallbackService {
    SendMessage buttonStart(Update update, String callbackData);
    EditMessageText buttonRegister(Update update);
    SendPhoto buttonPlace(Update update, String callbackData) throws IOException;

}
