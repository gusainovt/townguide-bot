package io.project.townguidebot.controller;

import io.project.townguidebot.dto.AdDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/ads")
@Tag(name = "Объявления")
public interface AdsController {

  @Operation(summary = "Получить все объявления", description = "Возвращает список всех объявлений.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Список объявлений успешно возвращен"),
      @ApiResponse(responseCode = "404", description = "Объявления не найдены"),
      @ApiResponse(responseCode = "500", description = "Ошибка сервера")
  })
  @GetMapping
  ResponseEntity<List<AdDto>> findAllAds();


  @Operation(summary = "Получить объявление по ID", description = "Возвращает объявление по указанному идентификатору.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Объявление успешно найдено"),
      @ApiResponse(responseCode = "404", description = "Объявление не найдено"),
      @ApiResponse(responseCode = "500", description = "Ошибка сервера")
  })
  @GetMapping("/{id}")
  ResponseEntity<AdDto> getAdById(@PathVariable long id);

  @Operation(summary = "Создать новое объявление", description = "Создает новое объявление на основе переданных данных.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Объявление успешно создано"),
      @ApiResponse(responseCode = "400", description = "Ошибка в данных объявления"),
      @ApiResponse(responseCode = "500", description = "Ошибка сервера")
  })
  @PostMapping
  ResponseEntity<AdDto> createAd(@RequestBody AdDto adDto);

  @Operation(summary = "Обновить существующее объявление", description = "Обновляет объявление по указанному идентификатору.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Объявление успешно обновлено"),
      @ApiResponse(responseCode = "404", description = "Объявление не найдено"),
      @ApiResponse(responseCode = "400", description = "Ошибка в данных объявления"),
      @ApiResponse(responseCode = "500", description = "Ошибка сервера")
  })
  @PutMapping("/{id}")
  ResponseEntity<AdDto> updateAd(@PathVariable long id, @RequestBody AdDto adDTO);

  @Operation(summary = "Удалить объявление по ID", description = "Удаляет объявление по указанному идентификатору.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Объявление успешно удалено"),
      @ApiResponse(responseCode = "404", description = "Объявление не найдено"),
      @ApiResponse(responseCode = "500", description = "Ошибка сервера")
  })
  @DeleteMapping("/{id}")
  ResponseEntity<Void> deleteAd(@PathVariable long id);
}
