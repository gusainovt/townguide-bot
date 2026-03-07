package io.project.townguidebot.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class StoryRq {
  @Schema(description = "Идентификатор города (city_id)")
  private Long cityId;

  @Schema(
      description = "Текст истории",
      example = "Боровск был основан в 1358 году и с тех пор стал важным культурным центром."
  )
  private String body;
}
