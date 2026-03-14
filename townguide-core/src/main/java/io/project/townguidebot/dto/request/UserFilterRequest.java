package io.project.townguidebot.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserFilterRequest {

  private String firstName;

  private String lastName;

  private String userName;

  private String phoneNumber;

  private LocalDateTime registeredFrom;

  private LocalDateTime registeredTo;

  private int page;

  private int size;
}
