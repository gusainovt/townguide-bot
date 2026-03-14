package io.project.townguidebot.dto;

import lombok.Builder;

@Builder
public record UploadedFile(
        byte[] bytes,
        String originalFilename,
        String contentType
) {
}

