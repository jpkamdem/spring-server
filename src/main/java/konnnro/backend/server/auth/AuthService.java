package konnnro.backend.server.auth;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import konnnro.backend.server.users.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final JWTService service;
  private final PasswordEncoder passwordEncoder;

  public Integer getExpiration() {
    return service.getExpirationDate();
  }

  public String generateToken(UUID id) {
    return service.generate(id);
  }

  public Object extract(String token) {
    return service.extractAllClaims(token);
  }

  public boolean authenticate(String rawPassword, String rawEmail, User storedUser) throws Exception {
    if (!rawEmail.equals(storedUser.getEmail())) {
      throw new Exception(
          "Erreur, les adresses mail ne correspondent pas");
    }

    if (!passwordEncoder.matches(rawPassword, storedUser.getPassword())) {
      throw new Exception(
          "Erreur, mot de passe invalide");
    }

    return true;
  }

}