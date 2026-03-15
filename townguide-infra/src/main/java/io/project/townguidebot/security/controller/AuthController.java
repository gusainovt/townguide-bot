package io.project.townguidebot.security.controller;

import io.project.townguidebot.security.JwtProvider;
import io.project.townguidebot.security.dto.AuthMeResponse;
import io.project.townguidebot.security.dto.ChangePasswordRequest;
import io.project.townguidebot.security.dto.LoginRequest;
import io.project.townguidebot.security.dto.RefreshRequest;
import io.project.townguidebot.security.dto.TokenResponse;
import io.project.townguidebot.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
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
  private final AuthService authService;

  @PostMapping("/login")
  public TokenResponse login(@RequestBody LoginRequest request) {
    var authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
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

  @GetMapping("/me")
  public AuthMeResponse me() {
    return authService.getCurrentUser();
  }

  @PostMapping("/change-password")
  public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordRequest request) {
    authService.changePassword(request);
    return ResponseEntity.noContent().build();
  }

  private TokenResponse buildTokenResponse(String username) {
    String accessToken = jwtProvider.generateAccessToken(username);
    String refreshToken = jwtProvider.generateRefreshToken(username);
    return new TokenResponse(accessToken, refreshToken);
  }
}
