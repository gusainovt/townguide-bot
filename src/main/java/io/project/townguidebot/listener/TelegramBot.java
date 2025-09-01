package io.project.townguidebot.listener;

import io.project.townguidebot.config.BotConfig;
import io.project.townguidebot.model.ButtonCallback;
import io.project.townguidebot.model.CommandType;
import io.project.townguidebot.model.MenuType;
import io.project.townguidebot.model.util.ButtonCallbackUtils;
import io.project.townguidebot.service.CallbackService;
import io.project.townguidebot.service.MenuService;
import io.project.townguidebot.service.UserService;
import io.project.townguidebot.service.strategy.CommandHandlerStrategy;
import io.project.townguidebot.service.strategy.MenuStrategy;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
@Slf4j
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {


    private final BotConfig config;
    private final UserService userService;
    private final MenuService menuService;
    private final CallbackService callbackService;

    private final List<CommandHandlerStrategy> commandStrategiesList;


    private final List<MenuStrategy> menuStrategyList;

    private Map<CommandType, CommandHandlerStrategy> commandHandlerStrategies;


    private Map<MenuType, MenuStrategy> menuStrategies;

    @PostConstruct
    public void init() {
        commandHandlerStrategies = commandStrategiesList.stream()
                .collect(Collectors.toMap(CommandHandlerStrategy::getCommandType, s -> s));

        menuStrategies = menuStrategyList.stream()
                .collect(Collectors.toMap(MenuStrategy::getMenuType, s -> s));


        //TODO: Вынести этот код в отдельный метод и вызывать его только после регистрации (добавить локализацию описаний).
//        log.info("Initialization bot menu");
//        List<BotCommand> listOfCommands = new ArrayList<>(List.of(
//                new BotCommand(COMMAND_START, DESCRIPTION_START),
//                new BotCommand(COMMAND_MY_DATA, DESCRIPTION_MY_DATA),
//                new BotCommand(COMMAND_DELETE_DATA, DESCRIPTION_DELETE_DATA),
//                new BotCommand(COMMAND_HELP, DESCRIPTION_HELP),
//                new BotCommand(COMMAND_SETTING, DESCRIPTION_SETTING),
//                new BotCommand(COMMAND_REGISTER, DESCRIPTION_REGISTER),
//                new BotCommand(COMMAND_STORY, DESCRIPTION_JOKE),
//                new BotCommand(COMMAND_WEATHER, DESCRIPTION_WEATHER)
//        ));
//        try {
//            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
//
//        } catch (TelegramApiException e) {
//            log.error(ERROR_SETTING + e.getMessage());
//        }

    }

    @Override
    public String getBotUsername() {
        log.info("Get bot name...");
        return config.getBotName();
    }
    @Override
    public String getBotToken() {
        log.info("Get bot token...");
        return config.getToken();
    }

    /**
     * Метод реализует основную логику взаимодействия с ботом через команды и кнопки.
     * @param update {@link Update} из библиотеки телеграмма
     */
    @Override
    @SneakyThrows
    public void onUpdateReceived(Update update) {

        Message message = Optional.ofNullable(update.getMessage()).orElseGet(() -> update.getCallbackQuery().getMessage());

        long chatId = message.getChatId();

        log.info("Starting bot for chat: {}", chatId);

        if (!userService.isRegisteredUser(chatId)) {
            userService.registeredUser(chatId, message.getChat());
        }

        if (update.hasMessage() && update.getMessage().hasText() && !update.hasCallbackQuery()) {
            String messageText = update.getMessage().getText();

            CommandHandlerStrategy commandHandlerStrategy = commandHandlerStrategies.get(CommandType.fromString(messageText));
            commandHandlerStrategy.handle(this, chatId);
        }

        if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            ButtonCallback buttonCallback = ButtonCallback.valueOf(callbackData);
            MenuType menuType = ButtonCallbackUtils.getMenuType(buttonCallback);

            MenuStrategy menuStrategy = menuStrategies.get(menuType);
            menuStrategy.handle(this, update);
        }
    }





}

