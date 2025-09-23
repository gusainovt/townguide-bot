package io.project.townguidebot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.io.IOException;

public interface SendingService {
    EditMessageText sendEditMessageText(String text, Long chatId, Long messageId);

    SendMessage sendMessage(Long chatId, String textToSend);

    SendMessage commandNotFound(Long chatId);

    SendPhoto sendStartPhoto(Long chatId, String caption) throws IOException;

    SendPhoto startCommandReceived(Long chatId);

    SendMessage sendWeather(Long chatId);

    SendPhoto sendPhoto(Long chatId, String path) throws IOException;

    SendMessage sendRandomStory(Long chatId);

    SendMessage sendHelpText(Long chatId);

    SendMessage cityMenuReceived(Long chatId);

    SendMessage selectCityCommandReceived(Long chatId);

    SendMessage sendRandomPlace(Long chatId);

    SendMessage cityNotSelected(Long chatId);
}
