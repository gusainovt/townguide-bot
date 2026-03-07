package io.project.townguidebot.integration;

import io.project.townguidebot.security.dto.LoginRequest;
import io.project.townguidebot.security.dto.RefreshRequest;
import io.project.townguidebot.security.dto.TokenResponse;
import io.project.townguidebot.security.model.AdminUser;
import io.project.townguidebot.security.repository.AdminUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
}
