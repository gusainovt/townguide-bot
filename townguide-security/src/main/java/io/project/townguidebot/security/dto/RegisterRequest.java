package io.project.townguidebot.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Registration request")
public class RegisterRequest {

  @Schema(description = "User login", example = "user")
  private String login;

  @Schema(description = "User password", example = "password")
  private String password;
}
