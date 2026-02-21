package io.project.townguidebot.security;

import io.project.townguidebot.security.service.AdminUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  private final JwtProvider jwtProvider;
  private final AdminUserDetailsService userDetailsService;

  @Override
  public void doFilterInternal(HttpServletRequest request,
                               HttpServletResponse response,
                               FilterChain filterChain) throws ServletException, IOException {
    try {

      String header = request.getHeader("Authorization");

      if (header != null && header.startsWith("Bearer ")
          && SecurityContextHolder.getContext().getAuthentication() == null) {

        String token = header.substring(7);

        if (jwtProvider.validateToken(token)) {

          String username = jwtProvider.getUsername(token);

          UserDetails user = userDetailsService.loadUserByUsername(username);

          UsernamePasswordAuthenticationToken auth =
              new UsernamePasswordAuthenticationToken(
                  user, null, user.getAuthorities());

          auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

          SecurityContextHolder.getContext().setAuthentication(auth);
        }

      }

    } catch (Exception e) {
      SecurityContextHolder.clearContext();
    }

    filterChain.doFilter(request, response);
  }
}
