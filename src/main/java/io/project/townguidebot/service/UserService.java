package io.project.townguidebot.service;

import io.project.townguidebot.dto.request.UserFilterRequest;
import io.project.townguidebot.dto.response.UsersResponse;
import io.project.townguidebot.model.User;
import java.util.List;
import org.telegram.telegrambots.meta.api.objects.Chat;

public interface UserService {
    List<User> findAllUsers();

    void registeredUser(Long chatId, Chat chat);

    Boolean isRegisteredUser(Long chatId);

    String getNameByChatId(Long chatId);

    UsersResponse findUsersByFilter(UserFilterRequest userFilterRequest);
}
