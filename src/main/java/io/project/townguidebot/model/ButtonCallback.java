package io.project.townguidebot.model;

import io.project.townguidebot.annotation.Menu;

public enum ButtonCallback {
    @Menu(MenuType.START)
    STORY,
    @Menu(MenuType.PLACE)
    PHOTO,
    @Menu(MenuType.START)
    PLACE,
    @Menu(MenuType.CITY)
    CITY,
    @Menu(MenuType.START)
    WEATHER,
    @Menu(MenuType.START)
    SELECT_CITY;

    public static ButtonCallback fromCallbackData(String callbackData) {
        if (callbackData.contains(":")) {
            String enumPart = callbackData.split(":")[0];
            return valueOf(enumPart);
        }
        return valueOf(callbackData);
    }
}
