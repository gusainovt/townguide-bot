package io.project.townguidebot.exception.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.project.townguidebot.exception.enums.ExType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
@Schema(description = "Ошибка")
public class ErrorResponse {

    @Schema(description = "Тип ошибки", example = "AD_NOT_FOUND")
    private ExType type;

    @Schema(description = "Текст ошибки")
    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

}
