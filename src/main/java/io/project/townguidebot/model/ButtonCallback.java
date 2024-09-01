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
    CITY
}
