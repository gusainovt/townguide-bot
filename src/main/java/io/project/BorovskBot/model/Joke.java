package io.project.BorovskBot.model;

import lombok.Data;

import jakarta.persistence.*;

@Entity
@Data
public class Joke {
    @Id
    private Integer id;

    @Column(length = 2550000)
    private String body;

    private String category;

    private double rating;
}
