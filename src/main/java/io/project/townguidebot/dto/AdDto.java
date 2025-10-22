package io.project.townguidebot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Объявление")
public class AdDto {

    @Schema(description = "Текст объявления", example = "Проверь не появилось ли что-то новое в боте!")
    private String ad;
}
