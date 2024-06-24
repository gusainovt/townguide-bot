package io.project.townguidebot.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Описание места в Боровске")
@Data
public class PlaceDto {
    @Schema(description = "Название места", example = "Пафнутейский монастырь")
    private String name;
    @Schema(description = "Описание места", example = "Пафнутейский монастырь — это исторический монастырь с богатой историей.")
    private String description;
}
