package io.project.townguidebot.controller;

import io.project.townguidebot.model.dto.StoryDto;
import io.project.townguidebot.service.StoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/stories")
@Tag(name = "Истории")
public class StoriesController {
    private final StoryService storyService;

    @Operation(summary = "Получить все истории", description = "Возвращает список всех историй о городе Боровск.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список историй успешно возвращен"),
            @ApiResponse(responseCode = "404", description = "Истории не найдены"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @GetMapping
    public ResponseEntity<List<StoryDto>> findAllStories() {
        return ResponseEntity.ok(storyService.findAllStories());
    }

    @Operation(summary = "Получить историю по ID", description = "Возвращает историю по указанному идентификатору.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "История успешно найдена"),
            @ApiResponse(responseCode = "404", description = "История не найдена"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @GetMapping("/{id}")
    public ResponseEntity<StoryDto> getStoryById(@PathVariable Long id) {
        return ResponseEntity.ok(storyService.findStoryById(id));
    }

    @Operation(summary = "Создать новую историю", description = "Создает новую историю на основе переданных данных.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "История успешно создана"),
            @ApiResponse(responseCode = "400", description = "Ошибка в данных истории"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @PostMapping
    public ResponseEntity<StoryDto> createStory(@RequestBody StoryDto storyDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(storyService.createStory(storyDto));
    }

    @Operation(summary = "Обновить существующую историю", description = "Обновляет историю по указанному идентификатору.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "История успешно обновлена"),
            @ApiResponse(responseCode = "404", description = "История не найдена"),
            @ApiResponse(responseCode = "400", description = "Ошибка в данных истории"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @PutMapping("/{id}")
    public ResponseEntity<StoryDto> updateStory(@PathVariable long id, @RequestBody StoryDto storyDto) {
        return ResponseEntity.ok(storyService.updateStory(id, storyDto));
    }

    @Operation(summary = "Удалить историю по ID", description = "Удаляет историю по указанному идентификатору.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "История успешно удалена"),
            @ApiResponse(responseCode = "404", description = "История не найдена"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStory(@PathVariable long id) {
        storyService.deleteStory(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получить случайную историю", description = "Возвращает случайную историю о городе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Случайная история успешно возвращена"),
            @ApiResponse(responseCode = "404", description = "Случайная история не найдена"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @GetMapping("/{cityName}/random")
    public ResponseEntity<StoryDto> getRandomStory(@PathVariable String cityName) {
        return ResponseEntity.ok(storyService.getRandomStoryForCity(cityName));
    }

}
