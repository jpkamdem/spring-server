package org.myapp.backend.users;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public List<User> index() {
    List<User> users = userRepository.findAll();
    return users;
  }

  public Optional<User> showEmail(String email) throws NoSuchElementException {
    Optional<User> user = userRepository.findByEmail(email);
    return user;
  }

  public Optional<User> showUsername(String username) throws NoSuchElementException {
    Optional<User> user = userRepository.findByUsername(username);
    return user;
  }

  public Optional<User> showPhoneNumber(String phoneNumber) throws NoSuchElementException {
    Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);
    return user;
  }

  public Optional<User> show(UUID id) throws NoSuchElementException {
    Optional<User> user = userRepository.findById(id);
    return user;
  }

  public User store(User newUser) throws IllegalArgumentException {
    User user = new User();
    user.setUsername(newUser.getUsername());
    user.setEmail(newUser.getEmail());
    user.setPassword(passwordEncoder.encode(newUser.getPassword()));
    user.setAge(newUser.getAge());
    user.setPhoneNumber(newUser.getPhoneNumber());
    user.setRole(newUser.getRole());

    return userRepository.save(user);
  }

  public User update(User newUser, UUID id) throws NoSuchElementException {
    return userRepository.findById(id)
        .map(foundUser -> {
          foundUser.setUsername(newUser.getUsername());
          foundUser.setEmail(newUser.getEmail());
          foundUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
          foundUser.setAge(newUser.getAge());
          foundUser.setPhoneNumber(newUser.getPhoneNumber());
          foundUser.setRole(newUser.getRole());
          foundUser.setUpdatedAt(newUser.getCreatedAt());
          return userRepository.save(foundUser);
        })
        .orElseThrow();
  }

  public void destroy(UUID id) throws NoSuchElementException {
    userRepository.deleteById(id);
  }

}