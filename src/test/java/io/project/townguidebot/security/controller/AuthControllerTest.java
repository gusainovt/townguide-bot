package io.project.townguidebot.security.controller;

import io.project.townguidebot.security.JwtProvider;
import io.project.townguidebot.security.dto.LoginRequest;
import io.project.townguidebot.security.dto.RefreshRequest;
import io.project.townguidebot.security.dto.TokenResponse;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        when(jwtProvider.generateAccessToken(eq("admin"))).thenReturn("access-token");
        when(jwtProvider.generateRefreshToken(eq("admin"))).thenReturn("refresh-token");

        TokenResponse resp = controller.login(request);

        assertNotNull(resp);
        assertEquals("access-token", resp.token());
        assertEquals("refresh-token", resp.refreshToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtProvider).generateAccessToken("admin");
        verify(jwtProvider).generateRefreshToken("admin");
    }

    @Test
    void refresh_ShouldReturnTokensForValidRefreshToken() {
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        JwtProvider jwtProvider = mock(JwtProvider.class);
        AuthController controller = new AuthController(authenticationManager, jwtProvider);

        when(jwtProvider.validateRefreshToken(eq("refresh-token"))).thenReturn(true);
        when(jwtProvider.getUsername(eq("refresh-token"))).thenReturn("admin");
        when(jwtProvider.generateAccessToken(eq("admin"))).thenReturn("new-access");
        when(jwtProvider.generateRefreshToken(eq("admin"))).thenReturn("new-refresh");

        TokenResponse resp = controller.refresh(new RefreshRequest("refresh-token"));

        assertNotNull(resp);
        assertEquals("new-access", resp.token());
        assertEquals("new-refresh", resp.refreshToken());
        verify(jwtProvider).validateRefreshToken("refresh-token");
        verify(jwtProvider).getUsername("refresh-token");
        verify(jwtProvider).generateAccessToken("admin");
        verify(jwtProvider).generateRefreshToken("admin");
    }

    @Test
    void refresh_ShouldThrowWhenRefreshTokenIsInvalid() {
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        JwtProvider jwtProvider = mock(JwtProvider.class);
        AuthController controller = new AuthController(authenticationManager, jwtProvider);

        when(jwtProvider.validateRefreshToken(eq("invalid-token"))).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> controller.refresh(new RefreshRequest("invalid-token")));
    }
}
