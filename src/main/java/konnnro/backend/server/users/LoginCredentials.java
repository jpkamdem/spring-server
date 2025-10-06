package konnnro.backend.server.users;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginCredentials {

  @Email(message = "Votre adresse mail est invalide")
  private String identifier;

  private String password;
}