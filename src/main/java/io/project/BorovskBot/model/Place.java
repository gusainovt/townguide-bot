package io.project.BorovskBot.model;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    @Column(length = 25500)
    private String description;
    @OneToMany
    private List<Photo> photo;

    public Place(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Place() {

    }
}
