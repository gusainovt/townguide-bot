package io.project.townguidebot.security.controller;

import io.project.townguidebot.security.JwtProvider;
import io.project.townguidebot.security.dto.LoginRequest;
import io.project.townguidebot.security.dto.RefreshRequest;
import io.project.townguidebot.security.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final JwtProvider jwtProvider;

  @PostMapping("/login")
  public TokenResponse login(@RequestBody LoginRequest request) {

    var authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getUsername(), request.getPassword())
    );

    return buildTokenResponse(authentication.getName());
  }

  @PostMapping("/refresh")
  public TokenResponse refresh(@RequestBody RefreshRequest request) {
    String refreshToken = request.refreshToken();

    if (!jwtProvider.validateRefreshToken(refreshToken)) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token invalid");
    }

    String username = jwtProvider.getUsername(refreshToken);
    return buildTokenResponse(username);
  }

  private TokenResponse buildTokenResponse(String username) {
    String accessToken = jwtProvider.generateAccessToken(username);
    String refreshToken = jwtProvider.generateRefreshToken(username);
    return new TokenResponse(accessToken, refreshToken);
  }
}
