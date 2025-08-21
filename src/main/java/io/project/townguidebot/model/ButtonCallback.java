package io.project.townguidebot.model;

import io.project.townguidebot.annotation.Menu;

public enum ButtonCallback {
    @Menu(MenuType.REG)
    LANGUAGE_CODE_RU,
    @Menu(MenuType.REG)
    CANCEL,
    @Menu(MenuType.START)
    STORY,
    @Menu(MenuType.PLACE)
    PHOTO,
    @Menu(MenuType.START)
    PLACE
}
