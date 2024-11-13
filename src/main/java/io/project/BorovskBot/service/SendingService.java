package io.project.BorovskBot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.io.IOException;

public interface SendingService {
    EditMessageText sendEditMessageText(String text, long chatId, long messageId);

    SendMessage sendMessage(long chatId, String textToSend);

    SendMessage prepareAndSendMessage(long chatId, String textToSend);

    SendMessage commandNotFound(long chatId);

    SendPhoto sendPhoto(Long chatId, String caption) throws IOException;
}
