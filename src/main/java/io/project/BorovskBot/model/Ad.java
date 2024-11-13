package io.project.BorovskBot.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "ads")
@Schema(description = "Модель объявления")
public class Ad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор объявления", example = "1")
    private long id;

    @Column(name = "ad")
    @Schema(description = "Текст объявления", example = "Проверь не появилось ли что-то новое в боте!")
    private String ad;
}
