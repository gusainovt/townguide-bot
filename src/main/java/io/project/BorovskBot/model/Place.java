package io.project.BorovskBot.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.util.List;

@Entity
@Getter
@Setter
public class Place extends AbstractPersistable<Long> {

    @Column(name = "name")
    private String name;

    @Column(name = "description", length = 25500)
    private String description;

    @OneToMany(cascade = CascadeType.ALL)
    @Column(name = "photo")
    private List<Photo> photo;
}
