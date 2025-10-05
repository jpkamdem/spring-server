package konnnro.backend.server.auth;

import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;

@Service
public class JWTService {

  @Value("${app.secret}")
  private String baseKey;

  @Value("${app.expiration}")
  @Getter
  private Integer expirationDate;

  private SecretKey getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(baseKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public String generate(UUID id) {
    return Jwts.builder()
        .claim("user_id", id)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + expirationDate))
        .signWith(getSigningKey())
        .compact();
  }

  public Object extractClaims(String token) throws JwtException {
    try {
      return Jwts.parser()
          .verifyWith(getSigningKey())
          .build()
          .parseSignedClaims(token)
          .getPayload();
    } catch (JwtException ex) {
      throw new JwtException(ex.getLocalizedMessage());
    }
  }

}