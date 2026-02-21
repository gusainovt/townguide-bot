package io.project.townguidebot.dto.response;

import io.project.townguidebot.dto.PlaceDto;
import io.project.townguidebot.dto.StoryDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Город")
public class CityResponse {

  @Schema(description = "Идентификатор города")
  private Long id;

  @Schema(description = "Название города")
  private String name;

  @Schema(description = "Название города в нижнем регистре", example = "borovsk")
  private String nameEng;

  @Schema(description = "Описание города")
  private String description;

  @Schema(description = "Ответ на вызов кнопки")
  private String callback;

  @Schema(description = "Ссылка на фотографию")
  private String photo;

  @Schema(description = "Список мест")
  private List<PlaceDto> places;

  @Schema(description = "Список историй")
  private List<StoryDto> stories;
}
