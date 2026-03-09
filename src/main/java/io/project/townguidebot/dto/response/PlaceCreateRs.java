package io.project.townguidebot.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "Ответ успешного создания места")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlaceCreateRs {

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
