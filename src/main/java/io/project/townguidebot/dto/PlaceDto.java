package io.project.townguidebot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Описание места в городе")
@Data
public class PlaceDto {

    private Long id;

    @Schema(description = "Идентификатор города (city_id)")
    private Long cityId;

    @Schema(description = "Название места", example = "Пафнутьевский монастырь")
    private String name;

    @Schema(
            description = "Описание места",
            example = "Пафнутьевский монастырь — это исторический монастырь с богатой историей."
    )
    private String description;
}

