package io.project.townguidebot.model.util;

import io.project.townguidebot.annotation.Menu;
import io.project.townguidebot.model.ButtonCallback;
import io.project.townguidebot.model.MenuType;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

@Slf4j
public class ButtonCallbackUtils {
    public static MenuType getMenuType(ButtonCallback buttonCallback) {
        try {
            Field field = buttonCallback.getClass().getField(buttonCallback.name());
            Menu annotation = field.getAnnotation(Menu.class);
            if (annotation != null) {
                return annotation.value();
            }
        } catch (NoSuchFieldException e) {
            log.error("Error with annotation Menu: {} ", e.getMessage());
        }
        throw new IllegalArgumentException("No MenuType found for " + buttonCallback.name());
    }
}
