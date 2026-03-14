package io.project.townguidebot.dto.telegram;

import lombok.Builder;

@Builder
public record TelegramChatDto(
        String firstName,
        String lastName,
        String userName
) {
}

