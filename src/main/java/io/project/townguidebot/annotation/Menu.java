package io.project.townguidebot.annotation;

import io.project.townguidebot.model.enums.MenuType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Menu {
    MenuType value();
}
