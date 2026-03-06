package io.project.townguidebot.integration;

import io.project.townguidebot.dto.request.UserFilterRequest;
import io.project.townguidebot.dto.response.UsersResponse;
import io.project.townguidebot.model.User;
import io.project.townguidebot.model.enums.LanguageCode;
import io.project.townguidebot.repository.UserRepository;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserControllerIT extends AbstractIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    void findUsersByFilter_WhenEmptyFilter_ShouldReturnAllPaged() {
        userRepository.save(user(1L, "Anna", "Ivanova", "ann1", "111"));
        userRepository.save(user(2L, "Ann", "Petrova", "ann2", "222"));
        userRepository.save(user(3L, "Boris", "Sidorov", "boris", "333"));

        UserFilterRequest req = UserFilterRequest.builder()
                .page(0)
                .size(10)
                .build();

        ResponseEntity<UsersResponse> resp = restTemplate.postForEntity("/api/v1/users", req, UsersResponse.class);

        assertEquals(200, resp.getStatusCode().value());
        assertNotNull(resp.getBody());
        assertNotNull(resp.getBody().getUsers());
        assertEquals(3, resp.getBody().getUsers().size());
    }

    @Test
    void findUsersByFilter_ByFirstName_ShouldReturnMatchingUsers() {
        userRepository.save(user(1L, "Anna", "Ivanova", "ann1", "111"));
        userRepository.save(user(2L, "Ann", "Petrova", "ann2", "222"));
        userRepository.save(user(3L, "Boris", "Sidorov", "boris", "333"));

        UserFilterRequest req = UserFilterRequest.builder()
                .firstName("ann")
                .page(0)
                .size(10)
                .build();

        ResponseEntity<UsersResponse> resp = restTemplate.postForEntity("/api/v1/users", req, UsersResponse.class);

        assertEquals(200, resp.getStatusCode().value());
        assertNotNull(resp.getBody());
        assertNotNull(resp.getBody().getUsers());
        assertEquals(2, resp.getBody().getUsers().size());
        assertTrue(resp.getBody().getUsers().stream().allMatch(u -> u.getFirstName().toLowerCase().contains("ann")));
    }

    @Test
    void findUsersByFilter_ByRegisteredAtRange_ShouldReturnMatchingUsers() {
        Timestamp oldTs = Timestamp.valueOf(LocalDateTime.now().minusDays(10));
        Timestamp recentTs = Timestamp.valueOf(LocalDateTime.now().minusHours(1));

        User old = user(1L, "Old", "User", "old", "111");
        old.setRegisteredAt(oldTs);
        userRepository.save(old);

        User recent = user(2L, "Recent", "User", "recent", "222");
        recent.setRegisteredAt(recentTs);
        userRepository.save(recent);

        UserFilterRequest req = UserFilterRequest.builder()
                .registeredFrom(LocalDateTime.now().minusDays(1))
                .registeredTo(LocalDateTime.now().plusDays(1))
                .page(0)
                .size(10)
                .build();

        ResponseEntity<UsersResponse> resp = restTemplate.postForEntity("/api/v1/users", req, UsersResponse.class);

        assertEquals(200, resp.getStatusCode().value());
        assertNotNull(resp.getBody());
        assertNotNull(resp.getBody().getUsers());
        assertEquals(1, resp.getBody().getUsers().size());
        assertEquals(2L, resp.getBody().getUsers().getFirst().getChatId());
    }

    @Test
    void findUsersByFilter_ByPhoneNumber_ShouldReturnSingleUser() {
        userRepository.save(user(1L, "Anna", "Ivanova", "ann1", "111"));
        userRepository.save(user(2L, "Boris", "Sidorov", "boris", "222"));

        UserFilterRequest req = UserFilterRequest.builder()
                .phoneNumber("222")
                .page(0)
                .size(10)
                .build();

        ResponseEntity<UsersResponse> resp = restTemplate.postForEntity("/api/v1/users", req, UsersResponse.class);

        assertEquals(200, resp.getStatusCode().value());
        assertNotNull(resp.getBody());
        assertNotNull(resp.getBody().getUsers());
        assertEquals(1, resp.getBody().getUsers().size());
        assertEquals(2L, resp.getBody().getUsers().getFirst().getChatId());
    }

    private static User user(Long chatId, String firstName, String lastName, String userName, String phoneNumber) {
        User user = new User();
        user.setChatId(chatId);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUserName(userName);
        user.setPhoneNumber(phoneNumber);
        user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));
        user.setLanguageCode(LanguageCode.RU);
        return user;
    }
}

