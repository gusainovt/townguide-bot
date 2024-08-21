package io.project.townguidebot.service;

import io.project.townguidebot.model.LanguageCode;
import io.project.townguidebot.model.User;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

public interface UserService {
    List<User> findAllUsers();

    void registeredUser(Message msg, LanguageCode languageCode);

    Boolean isRegisteredUser(Long chatId);

    String getNameByChatId(Long chatId);
}
