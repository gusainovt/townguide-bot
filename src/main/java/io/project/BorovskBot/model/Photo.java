package io.project.BorovskBot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Getter
@Setter
@Table(name = "photos")
public class Photo extends AbstractPersistable<Long> {

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_size")
    private long fileSize;

    @Column(name = "mediaType")
    private String mediaType;

    @JsonIgnore
    @Column(name = "data")
    private byte[] data;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @Column(name = "place")
    private Place place;
}
