package io.project.townguidebot.security.dto;

public record ChangePasswordRequest(
    String currentPassword,
    String newPassword
) {
}
