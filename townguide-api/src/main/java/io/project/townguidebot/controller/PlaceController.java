package io.project.townguidebot.controller;

import io.project.townguidebot.dto.PlaceDto;
import io.project.townguidebot.dto.request.PlaceCreateRq;
import io.project.townguidebot.dto.response.PlaceCreateRs;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/api/v1/places")
@Tag(name = "Places")
public interface PlaceController {

  @Operation(summary = "Получить место по ID", description = "Возвращает место по указанному идентификатору.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Место успешно найдено"),
      @ApiResponse(responseCode = "404", description = "Место не найдено"),
      @ApiResponse(responseCode = "500", description = "Ошибка сервера")
  })
  @GetMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN', 'USER_FREE')")
  ResponseEntity<PlaceDto> getPlaceById(@PathVariable Long id);

  @Operation(
      summary = "Создать новое место c фотографией",
      description = "Создает новое место и загружает фотографию"
  )
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Место успешно создано"),
      @ApiResponse(responseCode = "400", description = "Ошибка в данных"),
      @ApiResponse(responseCode = "500", description = "Ошибка сервера")
  })
  @PostMapping(
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE
  )
  @PreAuthorize("hasRole('ADMIN')")
  ResponseEntity<PlaceCreateRs> create(@RequestPart("data") PlaceCreateRq req, @RequestPart("file") MultipartFile file)
      throws IOException;

  @Operation(summary = "Обновить существующее место", description = "Обновляет место по указанному идентификатору.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Место успешно обновлено"),
      @ApiResponse(responseCode = "404", description = "Место не найдено"),
      @ApiResponse(responseCode = "400", description = "Ошибка в данных места"),
      @ApiResponse(responseCode = "500", description = "Ошибка сервера")
  })
  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  ResponseEntity<PlaceDto> updatePlace(@PathVariable Long id, @RequestBody PlaceDto placeDto);

  @Operation(summary = "Удалить место по ID", description = "Удаляет место по указанному идентификатору.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Место успешно удалено"),
      @ApiResponse(responseCode = "404", description = "Место не найдено"),
      @ApiResponse(responseCode = "500", description = "Ошибка сервера")
  })
  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  ResponseEntity<Void> deletePlace(@PathVariable Long id);

}
