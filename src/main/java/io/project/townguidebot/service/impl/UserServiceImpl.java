package io.project.townguidebot.service.impl;

import io.project.townguidebot.model.LanguageCode;
import io.project.townguidebot.model.User;
import io.project.townguidebot.repository.UserRepository;
import io.project.townguidebot.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.sql.Timestamp;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    /**
     * Находит всех пользователей
     * @return {@link List} cписок пользователей
     */
    @Override
    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        log.info("Finding all users...");
        return userRepository.findAll();
    }

    /**
     * Регистрация пользователя
     * @param msg {@link Message} из библиотеки телеграмма
     */
    @Override
    @Transactional
    public void registeredUser(Message msg, LanguageCode languageCode) {
        log.info("Start registration for user: {}", msg.getChatId());
        var chatId = msg.getChatId();
        var chat = msg.getChat();

        User user = new User();

        user.setChatId(chatId);
        user.setFirstName(chat.getFirstName());
        user.setLastName(chat.getLastName());
        user.setUserName(chat.getUserName());
        user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));
        user.setLanguageCode(languageCode);

        userRepository.save(user);
        log.info("Successfully registered user: {}", user);
    }

    /**
     * Проверяет зарегистрирован ли пользователь
     * @param chatId {@link Long} ID чата пользователя
     * @return {@link Boolean} возвращает true если пользователь зарегистрирован
     */
    @Override
    @Transactional(readOnly = true)
    public Boolean isRegisteredUser(Long chatId) {
        log.info("Checking for user in database...");
        return userRepository.existsUserByChatId(chatId);
    }

    @Override
    public String getNameByChatId(Long chatId) {
        log.info("Get user name for chat: {}", chatId);
        return userRepository.getNameByChatId(chatId);
    }


}
