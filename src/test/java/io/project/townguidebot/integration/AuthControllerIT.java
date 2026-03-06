package io.project.townguidebot.integration;

import io.project.townguidebot.security.dto.LoginRequest;
import io.project.townguidebot.security.dto.TokenResponse;
import io.project.townguidebot.security.model.AdminUser;
import io.project.townguidebot.security.repository.AdminUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
    }
}
