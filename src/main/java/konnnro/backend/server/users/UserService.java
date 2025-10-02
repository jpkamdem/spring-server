package konnnro.backend.server.users;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository repository;

  public List<User> index() {
    return repository.findAll();
  }

  public User show(UUID id) {
    return repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
  }

  public Optional<User> showEmail(String email) {
    return repository.findByEmail(email);
  }

  public User store(User user) {
    return repository.save(user);
  }

  public User update(User newUser, UUID id) {
    return repository.findById(id)
        .map(foundUser -> {
          foundUser.setFirstname(newUser.getFirstname());
          foundUser.setLastname(newUser.getLastname());
          foundUser.setEmail(newUser.getEmail());
          foundUser.setPassword(newUser.getPassword());
          foundUser.setAge(newUser.getAge());
          foundUser.setPhoneNumber(newUser.getPhoneNumber());
          foundUser.setRole(newUser.getRole());
          foundUser.setUpdatedAt(newUser.getCreatedAt());
          return repository.save(foundUser);
        })
        .orElseThrow(() -> new UserNotFoundException(id));
  }

  public void destroy(UUID id) {
    repository.deleteById(id);
  }

}