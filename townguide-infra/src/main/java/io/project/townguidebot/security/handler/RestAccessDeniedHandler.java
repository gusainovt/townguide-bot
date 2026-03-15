package io.project.townguidebot.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.project.townguidebot.security.dto.MessageResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RestAccessDeniedHandler implements AccessDeniedHandler {

  private final ObjectMapper objectMapper;

  @Override
  public void handle(
      HttpServletRequest request,
      HttpServletResponse response,
      AccessDeniedException accessDeniedException
  ) throws IOException {
    response.setStatus(HttpStatus.FORBIDDEN.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");
    objectMapper.writeValue(response.getWriter(), new MessageResponse(resolveMessage(request)));
  }

  private String resolveMessage(HttpServletRequest request) {
    if ("POST".equalsIgnoreCase(request.getMethod()) && "/api/v1/city".equals(request.getRequestURI())) {
      return "Недостаточно прав для создания города";
    }
    return "Недостаточно прав для выполнения запроса";
  }
}
