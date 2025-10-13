package konnnro.backend.server.auth;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import konnnro.backend.server.error.ErrorResponse;
import konnnro.backend.server.users.LoginCredentials;
import konnnro.backend.server.users.User;
import konnnro.backend.server.users.UserService;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/auth")
public class AuthController {
  private final AuthService authService;
  private final UserService userService;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public AuthController(AuthService authService, UserService userService, PasswordEncoder passwordEncoder) {
    this.authService = authService;
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
  }

  @PostMapping(path = "/register")
  ResponseEntity<?> register(@RequestBody @Valid User user, HttpServletResponse response)
      throws IllegalArgumentException {
    Optional<User> foundByEmailUser = userService.showEmail(user.getEmail());
    Optional<User> foundByUsernameUser = userService.showUsername(user.getUsername());
    Optional<User> foundByPhoneNumber = userService.showPhoneNumber(user.getPhoneNumber());
    Boolean foundUser = foundByEmailUser.isPresent() || foundByUsernameUser.isPresent()
        || foundByPhoneNumber.isPresent();

    if (foundUser) {
      ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST,
          "L'utilisateur.ice que vous souhaitez créer existe déjà");
      return new ResponseEntity<>(error.getError(), error.getStatus());
    }

    User storedUser = userService.store(user);
    authService.addJwtCookie(storedUser, response);
    return new ResponseEntity<>(storedUser, HttpStatus.OK);
  }

  @PostMapping(path = "/login")
  ResponseEntity<?> login(@RequestBody LoginCredentials credentials, HttpServletResponse response)
      throws NoSuchElementException, IllegalArgumentException {
    Optional<User> foundByEmailUser = userService.showEmail(credentials.getIdentifier());
    Optional<User> foundByUsernameUser = userService.showUsername(credentials.getIdentifier());

    Boolean isFoundUser = foundByEmailUser.isPresent() || foundByUsernameUser.isPresent();
    if (!isFoundUser) {
      ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST, "Utilisateur.ice introuvable");
      return new ResponseEntity<>(error.getError(), error.getStatus());
    }

    User foundUser = new User();
    if (foundByEmailUser.isPresent()) {
      foundUser = foundByEmailUser.get();
    } else {
      foundUser = foundByUsernameUser.get();
    }

    Boolean isValidPassword = passwordEncoder.matches(credentials.getPassword(), foundUser.getPassword());
    if (!isValidPassword) {
      ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST, "Le mot de passe ne correspond pas");
      return new ResponseEntity<>(error.getError(), error.getStatus());
    }

    authService.addJwtCookie(foundUser, response);
    return new ResponseEntity<>(foundUser, HttpStatus.OK);
  }

  @PostMapping(path = "/logout")
  ResponseEntity<?> logout(HttpServletResponse response) throws NoSuchElementException {
    authService.invalidateJwtCookie(response);
    return new ResponseEntity<>(HttpStatus.OK);
  }

}