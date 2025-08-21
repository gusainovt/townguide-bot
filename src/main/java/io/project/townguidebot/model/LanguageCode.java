package io.project.townguidebot.model;

import lombok.Getter;

@Getter
public enum LanguageCode {
    RU("ru");

    private final String value;

    LanguageCode(String value) {
        this.value = value;
    }

}
