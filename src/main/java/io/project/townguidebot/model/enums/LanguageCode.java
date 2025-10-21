package io.project.townguidebot.model.enums;

import lombok.Getter;

@Getter
public enum LanguageCode {
    RU("ru");

    private final String value;

    LanguageCode(String value) {
        this.value = value;
    }

}
