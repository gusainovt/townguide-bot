package io.project.townguidebot.service.impl;

import io.project.townguidebot.dto.telegram.TelegramChatDto;
import io.project.townguidebot.dto.request.UserFilterRequest;
import io.project.townguidebot.dto.response.UsersResponse;
import io.project.townguidebot.mapper.UserMapper;
import io.project.townguidebot.model.User;
import io.project.townguidebot.model.enums.LanguageCode;
import io.project.townguidebot.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void findAllUsers_ShouldReturnAllUsers() {
        List<User> users = List.of(new User(), new User());
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.findAllUsers();

        assertSame(users, result);
        verify(userRepository).findAll();
    }

    @Test
    void registeredUser_ShouldCreateAndSaveUserFromChat() {
        Long chatId = 100L;
        TelegramChatDto chat = TelegramChatDto.builder()
                .firstName("Alex")
                .lastName("Doe")
                .userName("alex")
                .build();

        userService.registeredUser(chatId, chat);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User saved = captor.getValue();
        assertEquals(chatId, saved.getChatId());
        assertEquals(chat.firstName(), saved.getFirstName());
        assertEquals(chat.lastName(), saved.getLastName());
        assertEquals(chat.userName(), saved.getUserName());
        assertNotNull(saved.getRegisteredAt());
        assertEquals(LanguageCode.RU, saved.getLanguageCode());
    }

    @Test
    void isRegisteredUser_ShouldDelegateToRepository() {
        when(userRepository.existsUserByChatId(100L)).thenReturn(true);

        Boolean result = userService.isRegisteredUser(100L);

        assertEquals(true, result);
        verify(userRepository).existsUserByChatId(100L);
    }

    @Test
    void getNameByChatId_ShouldReturnName() {
        when(userRepository.getNameByChatId(100L)).thenReturn("Alex");

        String result = userService.getNameByChatId(100L);

        assertEquals("Alex", result);
        verify(userRepository).getNameByChatId(100L);
    }

    @Test
    void findUsersByFilter_ShouldUsePageableAndMapResponse() {
        UserFilterRequest request = UserFilterRequest.builder()
                .page(2)
                .size(50)
                .build();
        @SuppressWarnings("unchecked")
        Page<User> page = (Page<User>) org.mockito.Mockito.mock(Page.class);
        UsersResponse response = UsersResponse.builder().users(List.of()).build();

        when(userRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        when(userMapper.toUsersResponse(page)).thenReturn(response);

        UsersResponse result = userService.findUsersByFilter(request);

        assertSame(response, result);
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(userRepository).findAll(any(Specification.class), pageableCaptor.capture());
        assertEquals(2, pageableCaptor.getValue().getPageNumber());
        assertEquals(50, pageableCaptor.getValue().getPageSize());
        verify(userMapper).toUsersResponse(page);
    }
}
