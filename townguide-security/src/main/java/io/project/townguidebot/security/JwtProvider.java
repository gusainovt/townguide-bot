package io.project.townguidebot.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

  private static final String TOKEN_TYPE_CLAIM = "tokenType";
  private final SecretKey key;
  private final long accessTokenExpiration;
  private final long refreshTokenExpiration;

  public JwtProvider(
      @Value("${jwt.secret}") String secret,
      @Value("${jwt.access-expiration-ms:900000}") long accessTokenExpiration,
      @Value("${jwt.refresh-expiration-ms:604800000}") long refreshTokenExpiration
  ) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.accessTokenExpiration = accessTokenExpiration;
    this.refreshTokenExpiration = refreshTokenExpiration;
  }

  public String generateAccessToken(String username) {
    return generateToken(username, TokenType.ACCESS, accessTokenExpiration);
  }

  public String generateRefreshToken(String username) {
    return generateToken(username, TokenType.REFRESH, refreshTokenExpiration);
  }

  public String getUsername(String token) {
    return parseClaims(token).getSubject();
  }

  public boolean validateAccessToken(String token) {
    return validateToken(token, TokenType.ACCESS);
  }

  public boolean validateRefreshToken(String token) {
    return validateToken(token, TokenType.REFRESH);
  }

  private String generateToken(String username, TokenType type, long expirationMillis) {
    return Jwts.builder()
        .setSubject(username)
        .claim(TOKEN_TYPE_CLAIM, type.name())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
        .signWith(key)
        .compact();
  }

  private boolean validateToken(String token, TokenType expectedType) {
    try {
      Claims claims = parseClaims(token);
      String tokenType = claims.get(TOKEN_TYPE_CLAIM, String.class);
      return expectedType.name().equals(tokenType);
    } catch (JwtException | IllegalArgumentException | NullPointerException e) {
      return false;
    }
  }

  private Claims parseClaims(String token) {
    return Jwts.parser()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  private enum TokenType {
    ACCESS,
    REFRESH
  }
}
