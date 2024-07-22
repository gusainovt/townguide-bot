package io.project.townguidebot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum CommandType {
    START("/start"),
    MY_DATA("/mydata"),
    DELETE_DATA("/deletedata"),
    HELP("/help"),
    SETTING("/setting"),
    SEND("/send"),
    STORY("/story"),
    ADD_PLACE("/addPlace"),
    WEATHER("/weather"),
    DEFAULT("default");

    private final String value;

    public static CommandType fromString(String value) {
        return Arrays.stream(CommandType.values())
                .filter(e -> e.getValue().equals(value))
                .findFirst()
                .orElse(DEFAULT);
    }
}
