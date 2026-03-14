package io.project.townguidebot.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Создание города")
public class CityCreateRq {

  @Schema(description = "Название города", example = "Боровск")
  private String name;

  @Schema(description = "Название города в нижнем регистре", example = "borovsk")
  private String nameEng;

  @Schema(description = "Описание города", example = "Боровск исторический город..")
  private String description;
}
