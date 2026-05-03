package io.project.townguidebot.controller;

import io.project.townguidebot.dto.request.CityCreateRq;
import io.project.townguidebot.dto.response.CityResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/city")
@Tag(name = "City")
public interface CityController {

  @Operation(summary = "Получить все города", description = "Возвращает список всех городов")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Список городов успешно получен"),
      @ApiResponse(responseCode = "404", description = "Города не найдены"),
      @ApiResponse(responseCode = "500", description = "Ошибка сервера")
  })
  @GetMapping
  @PreAuthorize("hasAnyRole('ADMIN', 'USER_FREE')")
  List<CityResponse> findAllCities();

  @Operation(summary = "Создать новый город", description = "Создает новый город")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Город успешно создан"),
      @ApiResponse(responseCode = "500", description = "Ошибка при создании города")
  })
  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  CityResponse create(@RequestBody CityCreateRq req);

}
