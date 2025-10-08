package konnnro.backend.server.auth;

import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import konnnro.backend.server.users.LoginCredentials;
import konnnro.backend.server.users.User;
import konnnro.backend.server.users.UserService;

@Service
public class AuthService {
  private final JWTService jwtService;
  private final PasswordEncoder passwordEncoder;
  private final UserService userService;

  @Autowired
  public AuthService(JWTService jwtService, PasswordEncoder passwordEncoder, UserService userService) {
    this.jwtService = jwtService;
    this.passwordEncoder = passwordEncoder;
    this.userService = userService;
  }

  public Integer getExpiration() {
    return jwtService.getExpirationDate();
  }

  public String generateToken(UUID id) {
    return jwtService.generate(id);
  }

  public Object extract(String token) {
    return jwtService.extractClaims(token);
  }

  public User authenticate(LoginCredentials credentials) throws Exception {
    User foundByEmailUser = userService.showEmail(credentials.getIdentifier());
    User foundByUsernameUser = userService.showUsername(credentials.getIdentifier());
    User storedUser = Objects.requireNonNullElse(foundByEmailUser, foundByUsernameUser);
    if (storedUser == null) {
      throw new Exception("Utilisateur.ice introuvable");
    }

    if (!passwordEncoder.matches(credentials.getPassword(), storedUser.getPassword())) {
      throw new Exception(
          "Le mot de passe ne correspond pas");
    }

    return storedUser;
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