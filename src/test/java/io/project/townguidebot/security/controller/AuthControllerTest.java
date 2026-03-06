package io.project.townguidebot.security.controller;

import io.project.townguidebot.security.JwtProvider;
import io.project.townguidebot.security.dto.LoginRequest;
import io.project.townguidebot.security.dto.TokenResponse;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthControllerTest {

    @Test
    void login_ShouldAuthenticateAndReturnJwtForAuthenticatedUsername() {
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        JwtProvider jwtProvider = mock(JwtProvider.class);
        AuthController controller = new AuthController(authenticationManager, jwtProvider);

        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("pass");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("admin");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtProvider.generateToken(eq("admin"))).thenReturn("jwt-token");

        TokenResponse resp = controller.login(request);

        assertNotNull(resp);
        assertEquals("jwt-token", resp.token());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtProvider).generateToken("admin");
    }
}

