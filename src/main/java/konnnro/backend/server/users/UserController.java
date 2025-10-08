package konnnro.backend.server.users;

import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.List;
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
@RequestMapping(path = "/api/users/")
public class UserController {
  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping(path = "/")
  ResponseEntity<?> all() throws Exception {
    List<User> users = userService.index();
    return new ResponseEntity<>(users, HttpStatus.OK);
  }

  @GetMapping(path = "/{id}")
  ResponseEntity<?> one(@PathVariable UUID id) throws Exception {
    User foundUser = userService.show(id);
    return new ResponseEntity<>(foundUser, HttpStatus.OK);
  }

  @PutMapping(path = "/{id}")
  ResponseEntity<?> edit(@RequestBody @Valid User user, @PathVariable UUID id) throws Exception {
    userService.update(user, id);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @DeleteMapping(path = "/{id}")
  ResponseEntity<?> delete(@PathVariable UUID id) throws Exception {
    userService.destroy(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }

}