package io.project.BorovskBot.service.impl;

import io.project.BorovskBot.model.User;
import io.project.BorovskBot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.sql.Timestamp;
import java.util.List;

import static io.project.BorovskBot.service.constants.LogText.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements io.project.BorovskBot.service.UserService {

    private final UserRepository userRepository;

    /**
     * Находит всех пользователей
     * @return Список пользователей {@link User}
     */
    @Override
    public List<User> findAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    /**
     * Регестрирует пользавтеля и сохранет в БД
     * @param msg Объект {@link Message} из библиотеки телеграмма
     */
    @Override
    public void registeredUser(Message msg) {
        log.info(METHOD_CALLED + Thread.currentThread().getStackTrace()[2].getMethodName());
        if (userRepository.findById(msg.getChatId()).isEmpty()) {
            var chatId = msg.getChatId();
            var chat = msg.getChat();

            User user = new User();

            user.setChatId(chatId);
            user.setFirstName(chat.getFirstName());
            user.setLastName(chat.getLastName());
            user.setUserName(chat.getUserName());
            user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));
            userRepository.save(user);
            log.info(REGISTERED + user);
        }
    }

}
