package io.project.townguidebot.security.controller;

import io.project.townguidebot.security.JwtProvider;
import io.project.townguidebot.security.dto.AuthMeResponse;
import io.project.townguidebot.security.dto.ChangePasswordRequest;
import io.project.townguidebot.security.dto.LoginRequest;
import io.project.townguidebot.security.dto.RegisterRequest;
import io.project.townguidebot.security.dto.RefreshRequest;
import io.project.townguidebot.security.dto.TokenResponse;
import io.project.townguidebot.security.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Auth")
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final JwtProvider jwtProvider;
  private final AuthService authService;

  @Operation(summary = "Login", description = "Authenticates a user by login and password and returns JWT tokens.")
  @PostMapping("/login")
  public TokenResponse login(@RequestBody LoginRequest request) {
    var authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getLoginOrUsername(), request.getPassword())
    );
    return buildTokenResponse(authentication.getName());
  }

  @Operation(summary = "Register", description = "Creates a free user with USER_FREE role.")
  @PostMapping("/register")
  public ResponseEntity<Void> register(@RequestBody RegisterRequest request) {
    authService.register(request);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @Operation(summary = "Refresh token", description = "Issues a new token pair by refresh token.")
  @PostMapping("/refresh")
  public TokenResponse refresh(@RequestBody RefreshRequest request) {
    String refreshToken = request.refreshToken();

    if (!jwtProvider.validateRefreshToken(refreshToken)) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token invalid");
    }

    String username = jwtProvider.getUsername(refreshToken);
    return buildTokenResponse(username);
  }

  @Operation(summary = "Current user", description = "Returns the authenticated user profile.")
  @GetMapping("/me")
  public AuthMeResponse me() {
    return authService.getCurrentUser();
  }

  @Operation(summary = "Change password", description = "Changes password for the authenticated user.")
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
