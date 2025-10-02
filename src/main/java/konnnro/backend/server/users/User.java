package konnnro.backend.server.users;

import java.sql.Timestamp;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
public class User {
  @Id
  @Getter(AccessLevel.PUBLIC)
  @GeneratedValue
  @Column(name = "id", nullable = false, unique = true)
  private UUID id;

  @Getter(AccessLevel.PUBLIC)
  @Setter(AccessLevel.PUBLIC)
  @Column(name = "firstname", nullable = false, length = 55)
  private String firstname;

  @Getter(AccessLevel.PUBLIC)
  @Setter(AccessLevel.PUBLIC)
  @Column(name = "lastname", nullable = false, length = 55)
  private String lastname;

  @Getter(AccessLevel.PUBLIC)
  @Setter(AccessLevel.PUBLIC)
  @Email(message = "Votre email est invaldie")
  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Getter(AccessLevel.PUBLIC)
  @Setter(AccessLevel.PUBLIC)
  @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,}$", message = "Votre mot de passe doit contenir au minimum : une lettre majuscule, une lettre minuscule, un nombre, un caractères spécial et faire 8 caractères")
  @Column(name = "password", nullable = false)
  private String password;

  @Getter(AccessLevel.PUBLIC)
  @Setter(AccessLevel.PUBLIC)
  @Column(name = "age", nullable = false, length = 3)
  private Integer age;

  @Enumerated(EnumType.STRING)
  @Getter(AccessLevel.PUBLIC)
  @Setter(AccessLevel.PUBLIC)
  @Column(name = "role", nullable = false)
  private Role role;

  @Getter(AccessLevel.PUBLIC)
  @Setter(AccessLevel.PUBLIC)
  @Pattern(regexp = "^0[(6|7)][0-9]{8}$", message = "Votre numéro de téléphone est invalide")
  @Column(name = "phone_number", nullable = false, unique = true, length = 10)
  private String phoneNumber;

  @CreationTimestamp
  @Getter(AccessLevel.PUBLIC)
  @Column(name = "created_at", nullable = false)
  private Timestamp createdAt;

  @UpdateTimestamp
  @Getter(AccessLevel.PUBLIC)
  @Setter(AccessLevel.PUBLIC)
  @Column(name = "updated_at", nullable = false)
  private Timestamp updatedAt;

  public enum Role {
    superadmin,
    admin,
    user
  }

}