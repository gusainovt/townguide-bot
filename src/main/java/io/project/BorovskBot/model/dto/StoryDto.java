package io.project.BorovskBot.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "История о Боровске")
public class StoryDto {

    @Schema(description = "Текст истории", example = "Боровск был основан в 1358 году и с тех пор стал важным культурным центром.")
    private String body;

}
