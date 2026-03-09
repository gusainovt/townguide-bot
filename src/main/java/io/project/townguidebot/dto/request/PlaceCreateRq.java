package io.project.townguidebot.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlaceCreateRq {

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
