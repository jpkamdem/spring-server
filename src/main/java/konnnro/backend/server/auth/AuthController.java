package konnnro.backend.server.auth;

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
  ResponseEntity<?> register(@RequestBody @Valid User user, HttpServletResponse response) throws Exception {
    Map<String, Object> body = new HashMap<>();
    User storedUser = userService.store(user);
    authService.addJwtCookie(storedUser, response);
    body.put("user", storedUser);
    return new ResponseEntity<>(body, HttpStatus.OK);
  }

  @PostMapping(path = "/login")
  ResponseEntity<?> login(@RequestBody @Valid LoginCredentials credentials, HttpServletResponse response)
      throws Exception {
    Map<String, Object> body = new HashMap<>();
    User foundByEmailUser = userService.showEmail(credentials.getIdentifier());
    User foundByUsernameUser = userService.showUsername(credentials.getIdentifier());
    User user = new User();

    if (foundByEmailUser == null) {
      user = foundByUsernameUser;
    }
    if (foundByUsernameUser == null) {
      user = foundByEmailUser;
    }

    authService.authenticate(credentials.getPassword(), credentials.getIdentifier(), user);
    authService.addJwtCookie(user, response);
    body.put("user", user);
    return new ResponseEntity<>(body, HttpStatus.OK);
  }

  @PostMapping(path = "/logout")
  ResponseEntity<?> logout(HttpServletResponse response) throws Exception {
    Map<String, String> body = new HashMap<>();
    authService.invalidateJwtCookie(response);
    body.put("message", "Utilisateur.ice déconnecté.e");
    return new ResponseEntity<>(body, HttpStatus.OK);
  }

}