package io.project.townguidebot.service.constants;

import static io.project.townguidebot.model.CommandType.*;

public class TelegramText {
    public static final String HELP_TEXT = "Этот бот-путеводитель познакомит тебя с историческим городом Боровском.\n\n" +
            "В боте поддерживаются следующие команды: \n\n " +
            "Нажми " + START + " чтобы увидеть приветсвие; \n\n" +
            "Нажми " + MY_DATA + " чтобы увидеть данные, котрые хранит бот о тебе;\n\n" +
            "Нажми " + DELETE_DATA + " чтобы удалить данные о себе;\n\n" +
            "Нажми " + WEATHER + " чтобы посмотреть погоду в Боровске;\n\n" +
            "Нажми " + SETTING + " чтобы перейти в настройки.\n\n";



    public static final String HELLO = "Привет, ";
    public static final String GREETING = "!\nРад знакомству!:rocket:";
    public static final String NAME_CITY = "borovsk";
    public static final String REGISTER_QUESTION = "Select language: ";
    public static final String REGISTER_CONFIRMATION = "Ты зарегистррирован!";
    public static final String REGISTER_CANCEL = "Отмена регистрации...";
    public static final String NOT_FOUND_COMMAND = "Извини, это пока не работает:cry:";
    public static final String TEXT_WEATHER =
            "Погода в Боровске  " +
                    "\nТемпература: %s градусов; " +
                    "\nОшущается как: %s градусов; " +
                    "\nСкорость ветра: %s м/с.";
}
