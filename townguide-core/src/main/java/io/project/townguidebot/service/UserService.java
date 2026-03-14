package io.project.townguidebot.service;

import io.project.townguidebot.dto.request.UserFilterRequest;
import io.project.townguidebot.dto.response.UsersResponse;
import io.project.townguidebot.dto.telegram.TelegramChatDto;
import io.project.townguidebot.model.User;
import java.util.List;

public interface UserService {
    List<User> findAllUsers();

    void registeredUser(Long chatId, TelegramChatDto chat);

    Boolean isRegisteredUser(Long chatId);

    String getNameByChatId(Long chatId);

    UsersResponse findUsersByFilter(UserFilterRequest userFilterRequest);
}
