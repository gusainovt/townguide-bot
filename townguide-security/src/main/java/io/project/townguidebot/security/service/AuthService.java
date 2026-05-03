package io.project.townguidebot.security.service;

import io.project.townguidebot.security.dto.AuthMeResponse;
import io.project.townguidebot.security.dto.ChangePasswordRequest;
import io.project.townguidebot.security.dto.RegisterRequest;

public interface AuthService {

  void register(RegisterRequest request);

  AuthMeResponse getCurrentUser();

  void changePassword(ChangePasswordRequest request);
}
