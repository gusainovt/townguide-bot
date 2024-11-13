package io.project.BorovskBot.service;

import io.project.BorovskBot.model.User;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

public interface UserService {
    List<User> findAllUsers();

    void registeredUser(Message msg);
}
