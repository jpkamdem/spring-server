package konnnro.backend.users;

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

  public Optional<User> show(UUID id) {
    return repository.findById(id);
  }

  public void store(User user) {
    repository.save(user);
  }

  public void update() {
    // ...
  }

  public void destroy(UUID id) {
    repository.deleteById(id);
  }

}