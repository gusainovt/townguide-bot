package io.project.BorovskBot.model;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

@Entity(name = "adsTable")
@Getter
@Setter
public class Ads {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String ad;
}
