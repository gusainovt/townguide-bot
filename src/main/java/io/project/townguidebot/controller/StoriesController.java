package io.project.townguidebot.controller;

import io.project.townguidebot.dto.StoryDto;
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

@RequestMapping("/stories")
@Tag(name = "Истории")
public interface StoriesController {

  @Operation(summary = "Получить все истории", description = "Возвращает список всех историй о городе Боровск.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Список историй успешно возвращен"),
      @ApiResponse(responseCode = "404", description = "Истории не найдены"),
      @ApiResponse(responseCode = "500", description = "Ошибка сервера")
  })
  @GetMapping
  ResponseEntity<List<StoryDto>> findAllStories();

  @Operation(summary = "Получить историю по ID", description = "Возвращает историю по указанному идентификатору.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "История успешно найдена"),
      @ApiResponse(responseCode = "404", description = "История не найдена"),
      @ApiResponse(responseCode = "500", description = "Ошибка сервера")
  })
  @GetMapping("/{id}")
  ResponseEntity<StoryDto> getStoryById(@PathVariable Long id);

  @Operation(summary = "Создать новую историю", description = "Создает новую историю на основе переданных данных.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "История успешно создана"),
      @ApiResponse(responseCode = "400", description = "Ошибка в данных истории"),
      @ApiResponse(responseCode = "500", description = "Ошибка сервера")
  })
  @PostMapping
  ResponseEntity<StoryDto> createStory(@RequestBody StoryDto storyDto);

  @Operation(summary = "Обновить существующую историю", description = "Обновляет историю по указанному идентификатору.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "История успешно обновлена"),
      @ApiResponse(responseCode = "404", description = "История не найдена"),
      @ApiResponse(responseCode = "400", description = "Ошибка в данных истории"),
      @ApiResponse(responseCode = "500", description = "Ошибка сервера")
  })
  @PutMapping("/{id}")
  ResponseEntity<StoryDto> updateStory(@PathVariable long id, @RequestBody StoryDto storyDto);

  @Operation(summary = "Удалить историю по ID", description = "Удаляет историю по указанному идентификатору.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "История успешно удалена"),
      @ApiResponse(responseCode = "404", description = "История не найдена"),
      @ApiResponse(responseCode = "500", description = "Ошибка сервера")
  })
  @DeleteMapping("/{id}")
  ResponseEntity<Void> deleteStory(@PathVariable long id);

  @Operation(summary = "Получить случайную историю", description = "Возвращает случайную историю о городе")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Случайная история успешно возвращена"),
      @ApiResponse(responseCode = "404", description = "Случайная история не найдена"),
      @ApiResponse(responseCode = "500", description = "Ошибка сервера")
  })
  @GetMapping("/{cityName}/random")
  ResponseEntity<StoryDto> getRandomStory(@PathVariable String cityName);

}
