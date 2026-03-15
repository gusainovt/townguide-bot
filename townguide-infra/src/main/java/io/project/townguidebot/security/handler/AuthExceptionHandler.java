package io.project.townguidebot.security.handler;

import io.project.townguidebot.security.dto.MessageResponse;
import io.project.townguidebot.security.exception.InvalidPasswordException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class AuthExceptionHandler {

  @ExceptionHandler(InvalidPasswordException.class)
  public ResponseEntity<MessageResponse> handleInvalidPasswordException(InvalidPasswordException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(ex.getMessage()));
  }

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<MessageResponse> handleResponseStatusException(ResponseStatusException ex) {
    return ResponseEntity.status(ex.getStatusCode()).body(new MessageResponse(ex.getReason()));
  }
}
