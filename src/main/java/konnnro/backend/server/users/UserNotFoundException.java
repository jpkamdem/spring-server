package konnnro.backend.server.users;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {
  UserNotFoundException(UUID id) {
    super("Utilisateur " + id + " introuvable");
  }
}