package io.project.townguidebot.security.controller;

import io.project.townguidebot.security.JwtProvider;
import io.project.townguidebot.security.dto.LoginRequest;
import io.project.townguidebot.security.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final JwtProvider jwtProvider;

  @PostMapping("/login")
  public TokenResponse login(@RequestBody LoginRequest request) {

    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getUsername(), request.getPassword())
    );

    String token = jwtProvider.generateToken(request.getUsername());
    return new TokenResponse(token);
  }
}
