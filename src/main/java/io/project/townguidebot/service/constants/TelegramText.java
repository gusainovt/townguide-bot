package io.project.townguidebot.service.constants;

import static io.project.townguidebot.model.CommandType.*;

public class TelegramText {
    public static final String HELP_TEXT = "Этот бот-путеводитель познакомит тебя. Расскажет тебе о выбранном городе. \n\n" +
            "В боте поддерживаются следующие команды: \n\n " +
            START.getValue() + " - запустить бота; \n\n" +
            SELECT.getValue() + " - выбрать город; \n\n" +
            STORY.getValue() + " - история о выбранном городе;\n\n" +
            PLACE.getValue() + " - интересное место в выбранном городе;\n\n" +
            WEATHER.getValue() + " - посмотреть погоду выбранном городе.";

    public static final String HELLO = "Привет, ";
    public static final String GREETING = "! Рад знакомству!:rocket: \nВыбери город: ";
    public static final String NOT_FOUND_COMMAND = "Извини, это пока не работает:cry:";
    public static final String TEXT_WEATHER =
            "%s погода: " +
                    "\nТемпература: %s градусов; " +
                    "\nОщущается как: %s градусов; " +
                    "\nСкорость ветра: %s м/с.";

    public static final String SELECT_CITY = "Выбери город: ";
    public static final String CITY_UNSELECTED = ":scream_cat: Нужно выбрать город..";
    public static final String SELECT_PLACE = "Выбери место: ";
    public static final String PLACE_UNSELECTED = "Выбери место :pouting_cat:";

    //описание команд
    public static final String START_DESCRIPTION = "запустить бота";
    public static final String HELP_DESCRIPTION = "информация как пользоваться этим ботом";
    public static final String STORY_DESCRIPTION = "история о выбранном городе";
    public static final String WEATHER_DESCRIPTION = "посмотреть погоду выбранном городе";
    public static final String SELECT_DESCRIPTION = "выбрать город";
    public static final String PLACE_DESCRIPTION = "интересное место в выбранном городе";
}
