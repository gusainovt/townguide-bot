package io.project.townguidebot.controller;

import io.project.townguidebot.model.dto.PlaceDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/places")
@Tag(name = "Места")
public interface PlaceController {

  @Operation(summary = "Получить место по ID", description = "Возвращает место по указанному идентификатору.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Место успешно найдено"),
      @ApiResponse(responseCode = "404", description = "Место не найдено"),
      @ApiResponse(responseCode = "500", description = "Ошибка сервера")
  })
  @GetMapping("/{id}")
  ResponseEntity<PlaceDto> getPlaceById(@PathVariable Long id);

  @Operation(summary = "Создать новое место", description = "Создает новое место на основе переданных данных.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Место успешно создано"),
      @ApiResponse(responseCode = "400", description = "Ошибка в данных места"),
      @ApiResponse(responseCode = "500", description = "Ошибка сервера")
  })
  @PostMapping
  ResponseEntity<PlaceDto> createPlace(@RequestBody PlaceDto placeDto);

  @Operation(summary = "Обновить существующее место", description = "Обновляет место по указанному идентификатору.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Место успешно обновлено"),
      @ApiResponse(responseCode = "404", description = "Место не найдено"),
      @ApiResponse(responseCode = "400", description = "Ошибка в данных места"),
      @ApiResponse(responseCode = "500", description = "Ошибка сервера")
  })
  @PutMapping("/{id}")
  ResponseEntity<PlaceDto> updatePlace(@PathVariable Long id, @RequestBody PlaceDto placeDto);

  @Operation(summary = "Удалить место по ID", description = "Удаляет место по указанному идентификатору.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Место успешно удалено"),
      @ApiResponse(responseCode = "404", description = "Место не найдено"),
      @ApiResponse(responseCode = "500", description = "Ошибка сервера")
  })
  @DeleteMapping("/{id}")
  ResponseEntity<Void> deletePlace(@PathVariable Long id);

}
