package konnnro.backend.users;

import java.sql.Timestamp;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
public class User {
  @Id
  @Getter(AccessLevel.PRIVATE)
  @GeneratedValue
  @Column(name = "id", nullable = false, unique = true)
  private UUID id;

  @Getter(AccessLevel.PRIVATE)
  @Setter(AccessLevel.PRIVATE)
  @Column(name = "firstname", nullable = false, length = 55)
  private String firstname;

  @Getter(AccessLevel.PRIVATE)
  @Setter(AccessLevel.PRIVATE)
  @Column(name = "lastname", nullable = false, length = 55)
  private String lastname;

  @Getter(AccessLevel.PRIVATE)
  @Setter(AccessLevel.PRIVATE)
  @Pattern(regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])", message = "Votre adresse mail est invalide")
  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Getter(AccessLevel.PRIVATE)
  @Setter(AccessLevel.PRIVATE)
  @Pattern(regexp = " ^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,}$", message = "Votre mot de passe doit contenir au minimum : une lettre majuscule, une lettre minuscule, un nombre, un caractères spécial et faire 8 caractères")
  @Column(name = "password", nullable = false)
  private String password;

  @Getter(AccessLevel.PRIVATE)
  @Setter(AccessLevel.PRIVATE)
  @Column(name = "age", nullable = false, length = 3)
  private Integer age;

  @Getter(AccessLevel.PRIVATE)
  @Setter(AccessLevel.PRIVATE)
  @Column(name = "role", nullable = false)
  private Role role;

  @Getter(AccessLevel.PRIVATE)
  @Setter(AccessLevel.PRIVATE)
  @Pattern(regexp = "^0[(6|7)][0-9]{8}$", message = "Votre numéro de téléphone est invalide")
  @Column(name = "phone_number", nullable = false, unique = true, length = 10)
  private String phoneNumber;

  @CreationTimestamp
  @Getter(AccessLevel.PRIVATE)
  @Column(name = "created_at", nullable = false)
  private Timestamp createdAt;

  @UpdateTimestamp
  @Getter(AccessLevel.PRIVATE)
  @Setter(AccessLevel.PRIVATE)
  @Column(name = "updated_at", nullable = false)
  private Timestamp updatedAt;

  public enum Role {
    superadmin,
    admin,
    user
  }

}