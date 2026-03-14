package io.project.townguidebot.controller.impl;

import io.project.townguidebot.controller.UserController;
import io.project.townguidebot.dto.request.UserFilterRequest;
import io.project.townguidebot.dto.response.UsersResponse;
import io.project.townguidebot.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

  private final UserService userService;

  @Override
  public ResponseEntity<UsersResponse> findUsersByFilter(UserFilterRequest userFilterRequest) {
    UsersResponse response = userService.findUsersByFilter(userFilterRequest);
    return ResponseEntity.ok(response);
  }
}
