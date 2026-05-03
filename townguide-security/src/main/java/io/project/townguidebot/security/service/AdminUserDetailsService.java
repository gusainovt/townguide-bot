package io.project.townguidebot.security.service;

import io.project.townguidebot.model.User;
import io.project.townguidebot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminUserDetailsService implements UserDetailsService {

  private final UserRepository repository;

  @Override
  public UserDetails loadUserByUsername(String username) {
    log.debug("Loading user by username '{}'", username);
    User user = repository.findByLogin(username)
        .orElseThrow(() -> {
          log.warn("User '{}' not found in database", username);
          return new UsernameNotFoundException("User not found");
        });

    log.debug("User '{}' found with role {}", user.getLogin(), user.getRole());

    return org.springframework.security.core.userdetails.User.builder()
        .username(user.getLogin())
        .password(user.getPasswordHash())
        .roles(user.getRole().name())
        .build();
  }
}
