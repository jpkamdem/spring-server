package konnnro.backend.server.users;

import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/api/users/")
@RequiredArgsConstructor
public class UserController {
  private final UserService service;

  @GetMapping(path = "/")
  ResponseEntity<?> all() {
    List<User> users = service.index();
    return new ResponseEntity<>(users, HttpStatus.OK);
  }

  @GetMapping(path = "/{id}")
  ResponseEntity<?> one(@PathVariable UUID id) {
    Map<String, String> body = new HashMap<>();
    try {
      body.clear();
      User foundUser = service.show(id);
      return new ResponseEntity<>(foundUser, HttpStatus.OK);
    } catch (Exception e) {
      body.clear();
      body.put("message", "Utilisateur introuvable");
      return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping(path = "/")
  ResponseEntity<?> create(@RequestBody @Valid User user) {
    Map<String, String> body = new HashMap<>();
    body.clear();
    Optional<User> foundUser = service.showEmail(user.getEmail());
    if (foundUser.isPresent()) {
      body.put("message", "L'adresse mail " + user.getEmail() + " est déjà utilisé");
      return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    service.store(user);
    body.put("message", "Utilisateur " + user.getFirstname() + " " + user.getLastname() + " créé(e)");
    return new ResponseEntity<>(body, HttpStatus.CREATED);
  }

  @PutMapping(path = "/{id}")
  ResponseEntity<?> edit(@RequestBody @Valid User user, @PathVariable UUID id) {
    Map<String, String> body = new HashMap<>();
    body.clear();
    Optional<User> foundUser = service.showEmail(user.getEmail());
    if (foundUser.isPresent()) {
      body.put("message", "L'adresse mail " + user.getEmail() + " est déjà utilisé");
      return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    service.update(user, id);
    body.put("message", "Utilisateur " + user.getFirstname() + " " + user.getLastname() + " modifié(e)");
    return new ResponseEntity<>(body, HttpStatus.OK);
  }

  @DeleteMapping(path = "/{id}")
  ResponseEntity<?> delete(@PathVariable UUID id) {
    Map<String, String> body = new HashMap<>();
    body.clear();
    try {
      User foundUser = service.show(id);
      service.destroy(id);
      body.put("message", "Utilisateur " + foundUser.getFirstname() + " " + foundUser.getLastname() + " supprimé(e)");
      return new ResponseEntity<>(body, HttpStatus.OK);
    } catch (Exception e) {
      body.clear();
      body.put("message", "Utilisateur introuvable");
      return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
  }

}