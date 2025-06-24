package io.project.townguidebot.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    private Long chatId;

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
}
