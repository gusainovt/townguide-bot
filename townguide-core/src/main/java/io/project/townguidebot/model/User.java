package io.project.townguidebot.model;

import io.project.townguidebot.model.enums.LanguageCode;
import io.project.townguidebot.model.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Data
@Table(name = "users")
public class User extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chat_id", unique = true)
    private Long chatId;

    @Column(name = "login", unique = true)
    private String login;

    @Column(name = "name")
    private String name;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "registered_at")
    private Timestamp registeredAt;

    @Column(name = "embedded_joke")
    private Boolean embeddedJoke;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "bio")
    private String bio;

    @Column(name = "description")
    private String description;

    @Column(name = "pinned_message")
    private String pinnedMessage;

    @Column(name = "language_code")
    @Enumerated(EnumType.STRING)
    private LanguageCode languageCode;
}
