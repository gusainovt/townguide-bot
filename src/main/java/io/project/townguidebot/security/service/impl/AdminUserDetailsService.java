package io.project.townguidebot.security.service.impl;

import io.project.townguidebot.security.model.AdminUser;
import io.project.townguidebot.security.repository.AdminUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminUserDetailsService implements UserDetailsService {

  private final AdminUserRepository repository;

  @Override
  public UserDetails loadUserByUsername(String username) {
    AdminUser user = repository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("Admin not found"));

    return User.builder()
        .username(user.getUsername())
        .password(user.getPasswordHash())
        .roles(user.getRole().name())
        .build();
  }
}
