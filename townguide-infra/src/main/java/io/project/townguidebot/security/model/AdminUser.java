package io.project.townguidebot.security.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import io.project.townguidebot.model.AuditableEntity;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "admin_users")
@Getter
@Setter
public class AdminUser extends AuditableEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String username;

  @Column
  private String login;

  @Column
  private String name;

  @Column
  private String fullName;

  @Column(nullable = false)
  private String passwordHash;

  @Enumerated(EnumType.STRING)
  private Role role;

  public enum Role {
    ADMIN,
    USER
  }
}
