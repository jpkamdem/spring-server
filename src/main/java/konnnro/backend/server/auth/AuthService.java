package konnnro.backend.server.auth;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import konnnro.backend.server.users.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final JWTService jwtService;
  private final PasswordEncoder passwordEncoder;

  public Integer getExpiration() {
    return jwtService.getExpirationDate();
  }

  public String generateToken(UUID id) {
    return jwtService.generate(id);
  }

  public Object extract(String token) {
    return jwtService.extractClaims(token);
  }

  public void authenticate(String rawPassword, String identifier, User storedUser) throws Exception {
    if (!passwordEncoder.matches(rawPassword, storedUser.getPassword())) {
      throw new Exception(
          "Le mot de passe ne correspond pas");
    }
  }

  public void addJwtCookie(User user, HttpServletResponse response) {
    Cookie cookie = new Cookie("token", generateToken(user.getId()));
    cookie.setSecure(true);
    cookie.setHttpOnly(true);
    cookie.setPath("/");
    cookie.setMaxAge(getExpiration() / 1000);
    response.addCookie(cookie);
  }

  public void invalidateJwtCookie(HttpServletResponse response) {
    Cookie cookie = new Cookie("token", null);
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    cookie.setSecure(true);
    cookie.setMaxAge(0);
    response.addCookie(cookie);
  }

}