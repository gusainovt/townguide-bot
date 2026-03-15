package io.project.townguidebot.security;

import io.project.townguidebot.security.service.AdminUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  private final JwtProvider jwtProvider;
  private final AdminUserDetailsService userDetailsService;

  @Override
  public void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain
  ) throws ServletException, IOException {
    try {
      String header = request.getHeader("Authorization");

      if (header != null && header.startsWith("Bearer ")
          && SecurityContextHolder.getContext().getAuthentication() == null) {
        String token = header.substring(7);
        log.debug("Processing bearer token for {} {}", request.getMethod(), request.getRequestURI());

        if (!jwtProvider.validateAccessToken(token)) {
          log.warn("Rejected access token for {} {}: token is invalid or expired", request.getMethod(), request.getRequestURI());
          SecurityContextHolder.clearContext();
          request.setAttribute("auth_error_message", "Access token недействителен или истек");
          filterChain.doFilter(request, response);
          return;
        }

        String username = jwtProvider.getUsername(token);
        log.debug("Resolved JWT subject '{}' for {} {}", username, request.getMethod(), request.getRequestURI());
        UserDetails user = userDetailsService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken auth =
            new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);
        log.debug("Authenticated '{}' with authorities {}", username, user.getAuthorities());
      }
    } catch (Exception e) {
      log.warn("JWT authentication failed for {} {}: {}", request.getMethod(), request.getRequestURI(), e.getMessage());
      SecurityContextHolder.clearContext();
      request.setAttribute("auth_error_message", "Access token недействителен или истек");
    }

    filterChain.doFilter(request, response);
  }
}
