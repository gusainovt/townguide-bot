package io.project.townguidebot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "buttons")
public class Button {

    @EmbeddedId
    private ButtonId buttonId;

    @Enumerated(EnumType.STRING)
    private ButtonCallback callback;

    private String textButton;
}
