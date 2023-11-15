package io.project.BorovskBot.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class StoryDto {

    @Schema(description = "Уникальный идентификатор истории", example = "1")
    private Long id;

    @Schema(description = "Текст истории", example = "Боровск был основан в 1358 году и с тех пор стал важным культурным центром.")
    private String body;

}
