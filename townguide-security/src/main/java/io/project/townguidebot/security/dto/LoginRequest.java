package io.project.townguidebot.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Login request")
public class LoginRequest {

  @Schema(description = "User login. Kept as username for backward compatibility.", example = "admin")
  private String username;

  @Schema(description = "User login.", example = "admin")
  private String login;

  @Schema(description = "User password", example = "password")
  private String password;

  public String getLoginOrUsername() {
    return login != null && !login.isBlank() ? login : username;
  }
}
