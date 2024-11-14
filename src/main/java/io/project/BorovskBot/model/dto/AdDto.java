package io.project.BorovskBot.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Модель объявления")
public class AdDto {

    @Schema(description = "Уникальный идентификатор объявления", example = "1")
    private Long id;

    @Schema(description = "Текст объявления", example = "Проверь не появилось ли что-то новое в боте!")
    private String ad;
}
