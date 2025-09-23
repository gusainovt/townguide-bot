package io.project.townguidebot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum CommandType {
    START("/start"),
    HELP("/help"),
    STORY("/story"),
    WEATHER("/weather"),
    SELECT("/select"),
    PLACE("/place"),
    DEFAULT("default");

    private final String value;

    public static CommandType fromString(String value) {
        return Arrays.stream(CommandType.values())
                .filter(e -> e.getValue().equals(value))
                .findFirst()
                .orElse(DEFAULT);
    }
}
