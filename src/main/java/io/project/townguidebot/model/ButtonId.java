package io.project.townguidebot.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@Embeddable
public class ButtonId {

    private String languageCode;

    @Enumerated(EnumType.STRING)
    private ButtonType type;

    @Enumerated(EnumType.STRING)
    private MenuType menuType;
}
