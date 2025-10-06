package konnnro.backend.server.users;

import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;

  public List<User> index() throws Exception {
    try {
      List<User> users = repository.findAll();
      return users;
    } catch (Exception ex) {
      throw new Exception(
          "Échec de la récupération des utilisateurs et utilisatrices");
    }
  }

  public User showEmail(String email) throws Exception {
    try {
      User user = repository.findByEmail(email);
      return user;
    } catch (Exception ex) {
      throw new Exception(
          "L'adresse mail " + email + " n'existe pas dans la base de données");
    }
  }

  public User showUsername(String username) throws Exception {
    try {
      User user = repository.findByUsername(username);
      return user;
    } catch (Exception ex) {
      throw new Exception(
        "Le nom d'utilisateur.ice " + username + " n'existe pas dans la base de donnée"
      );
    }
  }

  public User show(UUID id) throws Exception {
    try {
      User user = repository.findById(id).orElseThrow();
      return user;
    } catch (Exception ex) {
      throw new Exception(
          "Échec de la récupération de l'utilisateur.ice ID");
    }
  }

  public User store(User newUser) throws Exception {
    try {
      if (showEmail(newUser.getEmail()) != null) {
        throw new Exception(
            "Un compte utilise déjà cette adresse mail : " + newUser.getEmail());
      }

      User user = new User();
      user.setUsername(newUser.getUsername());
      user.setEmail(newUser.getEmail());
      user.setPassword(passwordEncoder.encode(newUser.getPassword()));
      user.setAge(newUser.getAge());
      user.setPhoneNumber(newUser.getPhoneNumber());
      user.setRole(newUser.getRole());

      return repository.save(user);
    } catch (Exception ex) {
      throw new Exception(
          ex.getLocalizedMessage());
    }
  }

  public User update(User newUser, UUID id) throws Exception {
    try {
      return repository.findById(id)
          .map(foundUser -> {
            foundUser.setUsername(newUser.getUsername());
            foundUser.setEmail(newUser.getEmail());
            foundUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
            foundUser.setAge(newUser.getAge());
            foundUser.setPhoneNumber(newUser.getPhoneNumber());
            foundUser.setRole(newUser.getRole());
            foundUser.setUpdatedAt(newUser.getCreatedAt());
            return repository.save(foundUser);
          })
          .orElseThrow();
    } catch (Exception ex) {
      throw new Exception(
          "Échec de la modification de l'utilisateur.ice");
    }
  }

  public void destroy(UUID id) throws Exception {
    try {
      repository.deleteById(id);
    } catch (Exception ex) {
      throw new Exception(
          "Échec de la suppression de l'utilisateur.ice");
    }
  }

}