package konnnro.backend.server.auth;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import konnnro.backend.server.users.LoginCredentials;
import konnnro.backend.server.users.User;
import konnnro.backend.server.users.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/auth")
public class AuthController {
  private final AuthService authService;
  private final UserService userService;

  @Autowired
  public AuthController(AuthService authService, UserService userService) {
    this.authService = authService;
    this.userService = userService;
  }

  @PostMapping(path = "/register")
  ResponseEntity<?> register(@RequestBody @Valid User user, HttpServletResponse response) throws Exception {
    User storedUser = userService.store(user);
    authService.addJwtCookie(storedUser, response);
    return new ResponseEntity<>(storedUser, HttpStatus.OK);
  }

  @PostMapping(path = "/login")
  ResponseEntity<?> login(@RequestBody LoginCredentials credentials, HttpServletResponse response)
      throws Exception {
    User authenticatedUser = authService.authenticate(credentials);
    authService.addJwtCookie(authenticatedUser, response);
    return new ResponseEntity<>(authenticatedUser, HttpStatus.OK);
  }

  @PostMapping(path = "/logout")
  ResponseEntity<?> logout(HttpServletResponse response) throws Exception {
    authService.invalidateJwtCookie(response);
    return new ResponseEntity<>(HttpStatus.OK);
  }

}