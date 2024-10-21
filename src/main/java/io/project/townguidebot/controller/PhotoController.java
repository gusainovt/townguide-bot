package io.project.townguidebot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/photos")
@Tag(name = "Фотографии")
public interface PhotoController {

  @Operation(summary = "Сохранить фотографию", description = "Сохраняет фотографию для указанного места.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Фотография успешно сохранена"),
      @ApiResponse(responseCode = "400", description = "Ошибка в данных фотографии"),
      @ApiResponse(responseCode = "404", description = "Место не найдено"),
      @ApiResponse(responseCode = "500", description = "Ошибка сервера")
  })
  @PostMapping(value = "/{placeId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  ResponseEntity<String> savePhoto(@PathVariable Long placeId,
      @RequestBody MultipartFile multipartFile) throws IOException;
}
