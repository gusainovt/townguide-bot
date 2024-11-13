package io.project.BorovskBot.service.constants;

import static io.project.BorovskBot.service.constants.Commands.*;

public class TelegramText {
    public static final String HELP_TEXT = "Этот бот-путеводитель познакомит тебя с историческим городом Боровском.\n\n" +
            "В боте поддерживаются следующие команды: \n\n " +
            "Нажми " + COMMAND_REGISTER + " для регистрации \n\n" +
            "Нажми " + COMMAND_START + " чтобы увидеть приветсвие; \n\n" +
            "Нажми " + COMMAND_MY_DATA + " чтобы увидеть данные, котрые хранит бот о тебе;\n\n" +
            "Нажми " + COMMAND_DELETE_DATA + " чтобы удалить данные о себе;\n\n" +
            "Нажми " + COMMAND_WEATHER + " чтобы посмотреть погоду в Боровске;\n\n" +
            "Нажми " + COMMAND_SETTING + " чтобы перейти в настройки.\n\n";
    public static final int MAX_JOKE_ID_MINUS_ONE = 3772;
    public static final String REPLIED_USER = "Replied to user ";

    public static final String HELLO = "Привет, ";
    public static final String GREETING = "!\n Рад знакомству! :rocket:";



}
