package io.project.BorovskBot.model;

import lombok.Data;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity(name = "userDataTable")
@Data
public class User {
    @Id
    private Long chatId;
    private String firstName;
    private String lastName;
    private String userName;
    private Timestamp registeredAt;
    private Boolean embedeJoke;
    private String phoneNumber;
    private Double latitude;
    private Double longitude;
    private String bio;
    private String description;
    private String pinnedMessage;
}
