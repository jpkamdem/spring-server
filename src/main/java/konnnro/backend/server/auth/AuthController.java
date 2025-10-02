package konnnro.backend.server.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import konnnro.backend.server.users.LoginCredentials;
import konnnro.backend.server.users.User;
import konnnro.backend.server.users.UserService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/api/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;
  private final UserService userService;

  @PostMapping(path = "/register")
  ResponseEntity<?> register(@RequestBody @Valid User user, HttpServletResponse response) {
    Map<String, String> body = new HashMap<>();
    body.clear();
    User foundUser = userService.showEmail(user.getEmail());
    if (foundUser != null) {
      body.put("message", "Cette adresse mail est déjà utilisée");
      return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    userService.store(user);
    String token = authService.generateToken(user.getId());
    Cookie cookie = new Cookie("token", token);
    cookie.setSecure(true);
    cookie.setHttpOnly(true);
    cookie.setPath("/");
    cookie.setMaxAge(authService.getExpiration());
    response.addCookie(cookie);

    body.put("message", "Utilisateur " + user.getFirstname() + " " + user.getLastname() + " créé(e) et authentifié(e)");

    return new ResponseEntity<>(body, HttpStatus.OK);
  }

  @PostMapping(path = "/login")
  ResponseEntity<?> login(@RequestBody @Valid LoginCredentials credentials, HttpServletResponse response) {
    Map<String, Object> body = new HashMap<>();
    body.clear();
    User foundUser = userService.showEmail(credentials.getEmail());
    if (foundUser == null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // if (foundUser == null) {
    // body.put("message", "Cette adresse mail n'est pas utilisée");
    // return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    // }

    // // password check
    String token = authService.generateToken(foundUser.getId());
    Cookie cookie = new Cookie("token", token);
    cookie.setSecure(true);
    cookie.setHttpOnly(true);
    cookie.setPath("/");
    cookie.setMaxAge(authService.getExpiration() / 1000);
    response.addCookie(cookie);

    body.put("message",
        "Utilisateur " + foundUser.getFirstname() + " " + foundUser.getLastname() + " connecté(e)");
    body.put("tokenInfo", authService.extract(token));

    return new ResponseEntity<>(body, HttpStatus.OK);
  }

  @PostMapping(path = "/logout")
  ResponseEntity<?> logout(HttpServletResponse response) {
    Map<String, String> body = new HashMap<>();
    Cookie cookie = new Cookie("token", null);
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    cookie.setSecure(true);
    cookie.setMaxAge(0);
    response.addCookie(cookie);

    body.put("message", "Utilisateur déconnecté(e)");
    return new ResponseEntity<>(body, HttpStatus.OK);
  }

}