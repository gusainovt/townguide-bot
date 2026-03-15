package io.project.townguidebot.security.service;

import io.project.townguidebot.security.dto.AuthMeResponse;
import io.project.townguidebot.security.dto.ChangePasswordRequest;

public interface AuthService {

  AuthMeResponse getCurrentUser();

  void changePassword(ChangePasswordRequest request);
}
