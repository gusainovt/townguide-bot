package io.project.townguidebot.service;

import io.project.townguidebot.model.User;
import org.telegram.telegrambots.meta.api.objects.Chat;

import java.util.List;

public interface UserService {
    List<User> findAllUsers();

    void registeredUser(Long chatId, Chat chat);

    Boolean isRegisteredUser(Long chatId);

    String getNameByChatId(Long chatId);
}
