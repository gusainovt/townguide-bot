package io.project.BorovskBot.model;

import lombok.Data;

import jakarta.persistence.*;

@Entity
@Data
@Table(name = "jokes")
public class Joke {

    @Id
    private Integer id;

    @Column(name = "body", length = 2550000)
    private String body;

    @Column(name = "category")
    private String category;

    @Column(name = "rating")
    private double rating;
}
