package konnnro.backend.server.users;

import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import konnnro.backend.server.error.ErrorResponse;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping(path = "/api/users")
public class UserController {
  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping(path = "/")
  ResponseEntity<?> all() {
    List<User> users = userService.index();
    return new ResponseEntity<>(users, HttpStatus.OK);
  }

  @GetMapping(path = "/{id}")
  ResponseEntity<?> one(@PathVariable UUID id) throws NoSuchElementException {
    Optional<User> foundUser = userService.show(id);
    if (!foundUser.isPresent()) {
      ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST, "Utilisateur.ice ID: " + id + " introuvable");

      return new ResponseEntity<>(error.getError(), error.getStatus());
    }

    return new ResponseEntity<>(foundUser, HttpStatus.OK);
  }

  @PutMapping(path = "/{id}")
  ResponseEntity<?> edit(@RequestBody @Valid User user, @PathVariable UUID id) throws NoSuchElementException {
    Optional<User> foundUser = userService.show(id);
    if (!foundUser.isPresent()) {
      ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST, "Utilisateur.ice ID: " + id + " introuvable");

      return new ResponseEntity<>(error.getError(), error.getStatus());
    }

    User updatedUser = userService.update(user, id);
    return new ResponseEntity<>(updatedUser, HttpStatus.OK);
  }

  @DeleteMapping(path = "/{id}")
  ResponseEntity<?> delete(@PathVariable UUID id) throws NoSuchElementException {
    Optional<User> foundUser = userService.show(id);
    if (!foundUser.isPresent()) {
      ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST, "Utilisateur.ice ID: " + id + " introuvable");

      return new ResponseEntity<>(error.getError(), error.getStatus());
    }

    userService.destroy(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }

}