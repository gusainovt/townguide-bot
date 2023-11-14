package io.project.BorovskBot.controller;

import io.project.BorovskBot.model.Ad;
import io.project.BorovskBot.service.AdsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/ads")
@Tag(name = "Объявления", description = "Управление объявлениями")
public class AdsController {

    private final AdsService adsService;
    @Operation(summary = "Получить все объявления", description = "Возвращает список всех объявлений.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список объявлений успешно возвращен"),
            @ApiResponse(responseCode = "404", description = "Объявления не найдены"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @GetMapping
    public ResponseEntity<List<Ad>> findAllAds() {
        return ResponseEntity.ok(adsService.findAllAds());
    }
}
