package io.project.townguidebot.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "places")
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description", length = 25500)
    private String description;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Photo> photo;

    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private City city;
}
