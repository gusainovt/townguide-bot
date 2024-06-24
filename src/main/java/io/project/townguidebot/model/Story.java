package io.project.townguidebot.model;

import lombok.Data;

import jakarta.persistence.*;

@Entity
@Data
@Table(name = "stories")
public class Story {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "body", length = 2550000)
    private String body;

}
