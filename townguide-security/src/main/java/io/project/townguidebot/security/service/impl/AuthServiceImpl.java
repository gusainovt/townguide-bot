package io.project.townguidebot.security.service.impl;

import io.project.townguidebot.security.dto.AuthMeResponse;
import io.project.townguidebot.security.dto.ChangePasswordRequest;
import io.project.townguidebot.security.dto.RegisterRequest;
import io.project.townguidebot.security.exception.InvalidPasswordException;
import io.project.townguidebot.security.model.AdminUser;
import io.project.townguidebot.security.repository.AdminUserRepository;
import io.project.townguidebot.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final AdminUserRepository adminUserRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  @Transactional
  public void register(RegisterRequest request) {
    String login = request.getLogin();
    if (login == null || login.isBlank() || request.getPassword() == null || request.getPassword().isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Login and password are required");
    }
    if (adminUserRepository.existsByUsername(login)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
    }

    AdminUser user = new AdminUser();
    user.setUsername(login);
    user.setLogin(login);
    user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
    user.setRole(AdminUser.Role.USER_FREE);
    adminUserRepository.save(user);
  }

  @Override
  @Transactional(readOnly = true)
  public AuthMeResponse getCurrentUser() {
    AdminUser user = getAuthenticatedUser();
    return new AuthMeResponse(
        user.getId(),
        user.getUsername(),
        user.getLogin() != null ? user.getLogin() : user.getUsername(),
        user.getName(),
        user.getFullName(),
        "ROLE_" + user.getRole().name()
    );
  }

  @Override
  @Transactional
  public void changePassword(ChangePasswordRequest request) {
    AdminUser user = getAuthenticatedUser();
    if (!passwordEncoder.matches(request.currentPassword(), user.getPasswordHash())) {
      throw new InvalidPasswordException("Текущий пароль неверный");
    }

    user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
    adminUserRepository.save(user);
  }

  private AdminUser getAuthenticatedUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();
    return adminUserRepository.findByUsername(username)
        .orElseThrow(() -> new IllegalStateException("Authenticated admin not found: " + username));
  }
}
