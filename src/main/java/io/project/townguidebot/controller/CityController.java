package io.project.townguidebot.controller;

import io.project.townguidebot.dto.response.CityResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/city")
@Tag(name = "City")
public interface CityController {

  @Operation(summary = "Получить все города", description = "Возвращает список всех городов.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Список городов успешно получен"),
      @ApiResponse(responseCode = "404", description = "Города не найдены"),
      @ApiResponse(responseCode = "500", description = "Ошибка сервера")
  })
  @GetMapping
  List<CityResponse> findAllCities();

}
