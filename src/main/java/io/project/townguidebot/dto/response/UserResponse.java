package io.project.townguidebot.dto.response;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserResponse {

  private Long chatId;

  private String firstName;

  private String lastName;

  private String userName;

  private Timestamp registeredAt;

  private Boolean embeddedJoke;

  private String phoneNumber;

  private Double latitude;

  private Double longitude;

  private String bio;

  private String description;
}
