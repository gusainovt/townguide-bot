package io.project.townguidebot.security.service;

import io.project.townguidebot.security.model.AdminUser;
import io.project.townguidebot.security.repository.AdminUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminUserDetailsService implements UserDetailsService {

  private final AdminUserRepository repository;

  @Override
  public UserDetails loadUserByUsername(String username) {
    log.debug("Loading admin user by username '{}'", username);
    AdminUser user = repository.findByUsername(username)
        .orElseThrow(() -> {
          log.warn("Admin user '{}' not found in database", username);
          return new UsernameNotFoundException("Admin not found");
        });

    log.debug("Admin user '{}' found with role {}", user.getUsername(), user.getRole());

    return User.builder()
        .username(user.getUsername())
        .password(user.getPasswordHash())
        .roles(user.getRole().name())
        .build();
  }
}
