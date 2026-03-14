package io.project.townguidebot.security.dto;

import lombok.Data;

@Data
public class LoginRequest {
  private String username;
  private String password;
}
