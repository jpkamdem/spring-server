package konnnro.backend.server.auth;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final JWTService service;

  public Integer getExpiration() {
    return service.getExpirationDate();
  }

  public String generateToken(UUID id) {
    return service.generate(id);
  }

  public Object extract(String token) {
    return service.extractAllClaims(token);
  }

}