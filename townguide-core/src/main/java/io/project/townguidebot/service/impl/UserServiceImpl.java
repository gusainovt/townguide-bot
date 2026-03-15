package io.project.townguidebot.service.impl;

import io.project.townguidebot.dto.request.UserFilterRequest;
import io.project.townguidebot.dto.response.UsersResponse;
import io.project.townguidebot.dto.telegram.TelegramChatDto;
import io.project.townguidebot.mapper.UserMapper;
import io.project.townguidebot.model.User;
import io.project.townguidebot.model.enums.LanguageCode;
import io.project.townguidebot.repository.UserRepository;
import io.project.townguidebot.service.UserService;
import io.project.townguidebot.specification.UserSpecification;
import java.sql.Timestamp;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    /**
     * Находит всех пользователей
     * @return {@link List} cписок пользователей
     */
    @Override
    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        log.debug("Finding all users");
        return userRepository.findAll();
    }

    /**
     * Регистрация пользователя
     * @param chatId чат айди
     * @param chat {@link Chat} из библиотеки телеграм
     */
    @Override
    @Transactional
    public void registeredUser(Long chatId, TelegramChatDto chat) {
        log.debug("Start registration for user: {}", chatId);
        User user = new User();

        user.setChatId(chatId);
        user.setFirstName(chat.firstName());
        user.setLastName(chat.lastName());
        user.setUserName(chat.userName());
        user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));
        user.setLanguageCode(LanguageCode.RU);

        userRepository.save(user);
        log.debug("Successfully registered user with chatId: {}", chatId);
    }

    /**
     * Проверяет зарегистрирован ли пользователь
     * @param chatId {@link Long} ID чата пользователя
     * @return {@link Boolean} возвращает true если пользователь зарегистрирован
     */
    @Override
    @Transactional(readOnly = true)
    public Boolean isRegisteredUser(Long chatId) {
        log.debug("Checking for user in database by chatId: {}", chatId);
        //TODO: Добавить кеширование
        return userRepository.existsUserByChatId(chatId);
    }

    /**
     * Получить имя пользователя по id чата
     * @param chatId id чата
     * @return имя пользователя {@link String}
     */
    @Transactional(readOnly = true)
    @Override
    public String getNameByChatId(Long chatId) {
        log.debug("Get user name for chat: {}", chatId);
        return userRepository.getNameByChatId(chatId);
    }


    @Override
    @Transactional(readOnly = true)
    public UsersResponse findUsersByFilter(UserFilterRequest userFilterRequest) {
        int page = userFilterRequest.getPage();
        int size = userFilterRequest.getSize();

        log.debug("Get users by filter page: {}, size {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Specification<User> specification = UserSpecification.filterBy(userFilterRequest);

        Page<User> users = userRepository.findAll(specification, pageable);

        return userMapper.toUsersResponse(users);
    }


}
