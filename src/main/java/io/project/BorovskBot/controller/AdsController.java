package io.project.BorovskBot.controller;

import io.project.BorovskBot.model.dto.AdDto;
import io.project.BorovskBot.service.AdsService;
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
@RequestMapping("/ads")
@Tag(name = "Объявления")
public class AdsController {

    private final AdsService adsService;

    @Operation(summary = "Получить все объявления", description = "Возвращает список всех объявлений.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список объявлений успешно возвращен"),
            @ApiResponse(responseCode = "404", description = "Объявления не найдены"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @GetMapping
    public ResponseEntity<List<AdDto>> findAllAds() {
        return ResponseEntity.ok(adsService.findAllAds());
    }

    @Operation(summary = "Получить объявление по ID", description = "Возвращает объявление по указанному идентификатору.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Объявление успешно найдено"),
            @ApiResponse(responseCode = "404", description = "Объявление не найдено"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AdDto> getAdById(@PathVariable long id) {
        return ResponseEntity.ok(adsService.findAdById(id));
    }

    @Operation(summary = "Создать новое объявление", description = "Создает новое объявление на основе переданных данных.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Объявление успешно создано"),
            @ApiResponse(responseCode = "400", description = "Ошибка в данных объявления"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @PostMapping
    public ResponseEntity<AdDto> createAd(@RequestBody AdDto adDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adsService.createAd(adDto));
    }

    @Operation(summary = "Обновить существующее объявление", description = "Обновляет объявление по указанному идентификатору.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Объявление успешно обновлено"),
            @ApiResponse(responseCode = "404", description = "Объявление не найдено"),
            @ApiResponse(responseCode = "400", description = "Ошибка в данных объявления"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AdDto> updateAd(@PathVariable long id, @RequestBody AdDto adDTO) {
        return ResponseEntity.ok(adsService.updateAd(id, adDTO));
    }
    @Operation(summary = "Удалить объявление по ID", description = "Удаляет объявление по указанному идентификатору.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Объявление успешно удалено"),
            @ApiResponse(responseCode = "404", description = "Объявление не найдено"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAd(@PathVariable long id) {
        adsService.deleteAd(id);
        return ResponseEntity.noContent().build();
    }
}
