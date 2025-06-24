package io.project.townguidebot.controller;

import io.project.townguidebot.model.dto.PlaceDto;
import io.project.townguidebot.service.PlaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/places")
@Tag(name = "Места")
public class PlaceController {

    private final PlaceService placeService;

    @Operation(summary = "Получить место по ID", description = "Возвращает место по указанному идентификатору.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Место успешно найдено"),
            @ApiResponse(responseCode = "404", description = "Место не найдено"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PlaceDto> getPlaceById(@PathVariable Long id) {
        PlaceDto placeDto = placeService.findPlaceById(id);
        return ResponseEntity.ok(placeDto);
    }

    @Operation(summary = "Создать новое место", description = "Создает новое место на основе переданных данных.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Место успешно создано"),
            @ApiResponse(responseCode = "400", description = "Ошибка в данных места"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @PostMapping
    public ResponseEntity<PlaceDto> createPlace(@RequestBody PlaceDto placeDto) {
        PlaceDto createdPlace = placeService.createPlace(placeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPlace);
    }

    @Operation(summary = "Обновить существующее место", description = "Обновляет место по указанному идентификатору.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Место успешно обновлено"),
            @ApiResponse(responseCode = "404", description = "Место не найдено"),
            @ApiResponse(responseCode = "400", description = "Ошибка в данных места"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PlaceDto> updatePlace(@PathVariable Long id, @RequestBody PlaceDto placeDto) {
        PlaceDto updatedPlace = placeService.updatePlace(id, placeDto);
        return ResponseEntity.ok(updatedPlace);
    }

    @Operation(summary = "Удалить место по ID", description = "Удаляет место по указанному идентификатору.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Место успешно удалено"),
            @ApiResponse(responseCode = "404", description = "Место не найдено"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlace(@PathVariable Long id) {
        placeService.deletePlace(id);
        return ResponseEntity.noContent().build();
    }
}
