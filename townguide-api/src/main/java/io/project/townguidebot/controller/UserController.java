package io.project.townguidebot.controller;

import io.project.townguidebot.dto.request.UserFilterRequest;
import io.project.townguidebot.dto.response.UsersResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/users")
@Tag(name = "Users")
public interface UserController {

  @Operation(summary = "Получить пользователей по заданным полям", description = "Возвращает список всех пользователей подходящих под фильтр")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Список пользователей успешно возвращен"),
      @ApiResponse(responseCode = "404", description = "пользователи не найдены"),
      @ApiResponse(responseCode = "500", description = "Ошибка сервера")
  })
  @PostMapping
  @PreAuthorize("hasAnyRole('ADMIN', 'USER_FREE')")
  ResponseEntity<UsersResponse> findUsersByFilter(@RequestBody UserFilterRequest userFilterRequest);

}
