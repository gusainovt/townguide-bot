package io.project.townguidebot.integration;

import io.project.townguidebot.security.dto.AuthMeResponse;
import io.project.townguidebot.security.dto.ChangePasswordRequest;
import io.project.townguidebot.security.dto.LoginRequest;
import io.project.townguidebot.security.dto.MessageResponse;
import io.project.townguidebot.security.dto.RefreshRequest;
import io.project.townguidebot.security.dto.TokenResponse;
import io.project.townguidebot.security.model.AdminUser;
import io.project.townguidebot.security.repository.AdminUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuthControllerIT extends AbstractIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        adminUserRepository.deleteAll();
        AdminUser admin = new AdminUser();
        admin.setUsername("admin");
        admin.setLogin("admin");
        admin.setName("Иван");
        admin.setFullName("Иван Петров");
        admin.setPasswordHash(passwordEncoder.encode("pass"));
        admin.setRole(AdminUser.Role.ADMIN);
        adminUserRepository.save(admin);
    }

    @Test
    void login_ShouldReturnJwtToken() {
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("pass");

        ResponseEntity<TokenResponse> resp = restTemplate.postForEntity("/auth/login", request, TokenResponse.class);

        assertEquals(200, resp.getStatusCode().value());
        assertNotNull(resp.getBody());
        assertNotNull(resp.getBody().token());
        assertFalse(resp.getBody().token().isBlank());
        assertNotNull(resp.getBody().refreshToken());
        assertFalse(resp.getBody().refreshToken().isBlank());
    }

    @Test
    void refresh_ShouldReturnNewTokens() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("pass");

        ResponseEntity<TokenResponse> loginResp = restTemplate.postForEntity("/auth/login", loginRequest, TokenResponse.class);
        assertEquals(200, loginResp.getStatusCode().value());
        assertNotNull(loginResp.getBody());
        assertNotNull(loginResp.getBody().refreshToken());

        String oldAccessToken = loginResp.getBody().token();
        RefreshRequest refreshRequest = new RefreshRequest(loginResp.getBody().refreshToken());

        ResponseEntity<TokenResponse> refreshResp = restTemplate.postForEntity("/auth/refresh", refreshRequest, TokenResponse.class);
        assertEquals(200, refreshResp.getStatusCode().value());
        assertNotNull(refreshResp.getBody());
        assertNotNull(refreshResp.getBody().token());
        assertNotNull(refreshResp.getBody().refreshToken());
        assertFalse(refreshResp.getBody().token().isBlank());
        assertNotEquals(oldAccessToken, refreshResp.getBody().token());
    }

    @Test
    void refresh_ShouldRejectInvalidToken() {
        RefreshRequest refreshRequest = new RefreshRequest("invalid.token");

        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                restTemplate.postForEntity("/auth/refresh", refreshRequest, TokenResponse.class));

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
    }

    @Test
    void me_ShouldReturnCurrentUserProfile() {
        String accessToken = loginAndGetAccessToken("admin", "pass");
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        ResponseEntity<AuthMeResponse> response = restTemplate.exchange(
                "/auth/me",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                AuthMeResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("admin", response.getBody().username());
        assertEquals("admin", response.getBody().login());
        assertEquals("Иван", response.getBody().name());
        assertEquals("Иван Петров", response.getBody().fullName());
        assertEquals("ROLE_ADMIN", response.getBody().role());
    }

    @Test
    void changePassword_ShouldUpdatePassword() {
        String accessToken = loginAndGetAccessToken("admin", "pass");
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        ResponseEntity<Void> response = restTemplate.exchange(
                "/auth/change-password",
                HttpMethod.POST,
                new HttpEntity<>(new ChangePasswordRequest("pass", "new-pass-123"), headers),
                Void.class
        );

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertTrue(passwordEncoder.matches(
                "new-pass-123",
                adminUserRepository.findByUsername("admin").orElseThrow().getPasswordHash()
        ));
    }

    @Test
    void changePassword_ShouldReturnBadRequestWhenCurrentPasswordIsWrong() {
        String accessToken = loginAndGetAccessToken("admin", "pass");
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        ResponseEntity<MessageResponse> response = restTemplate.exchange(
                "/auth/change-password",
                HttpMethod.POST,
                new HttpEntity<>(new ChangePasswordRequest("wrong-pass", "new-pass-123"), headers),
                MessageResponse.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Текущий пароль неверный", response.getBody().message());
    }

    private String loginAndGetAccessToken(String username, String password) {
        LoginRequest request = new LoginRequest();
        request.setUsername(username);
        request.setPassword(password);

        ResponseEntity<TokenResponse> response = restTemplate.postForEntity("/auth/login", request, TokenResponse.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        return response.getBody().token();
    }
}
