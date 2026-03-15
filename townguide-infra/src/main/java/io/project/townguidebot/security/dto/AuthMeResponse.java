package io.project.townguidebot.security.dto;

public record AuthMeResponse(
    Long id,
    String username,
    String login,
    String name,
    String fullName,
    String role
) {
}
