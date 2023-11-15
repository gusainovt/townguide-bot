package io.project.BorovskBot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "photos.sql")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_size")
    private long fileSize;

    @Column(name = "mediaType")
    private String mediaType;

    @Lob
    @JsonIgnore
    @Column(name = "data")
    private byte[] data;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.REMOVE)
    private Place place;
}
