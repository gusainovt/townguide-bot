package io.project.townguidebot.listener;

import io.project.townguidebot.config.BotConfig;
import io.project.townguidebot.exception.EmptyMessageException;
import io.project.townguidebot.model.enums.ButtonCallback;
import io.project.townguidebot.model.enums.CommandType;
import io.project.townguidebot.model.enums.MenuType;
import io.project.townguidebot.model.util.ButtonCallbackUtils;
import io.project.townguidebot.service.CityService;
import io.project.townguidebot.service.CommandService;
import io.project.townguidebot.service.PlaceService;
import io.project.townguidebot.service.UserService;
import io.project.townguidebot.service.strategy.CommandHandlerStrategy;
import io.project.townguidebot.service.strategy.MenuStrategy;
import io.project.townguidebot.service.util.MessageExtractor;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;


@Component
@Slf4j
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {


    private final BotConfig config;
    private final UserService userService;
    private final CityService cityService;
    private final PlaceService placeService;
    private final CommandService commandService;

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

        commandService.initCommands(this);
    }

    @Override
    public String getBotUsername() {
        log.info("Get bot name...");
        return config.getName();
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

        Message message = MessageExtractor.extract(update).orElseThrow(()->{
            log.error("Message is empty or not found");
            return new EmptyMessageException("Message is empty or not found");
        });

        long chatId = message.getChatId();

        log.info("Starting bot for chat: {}", chatId);

        if (!userService.isRegisteredUser(chatId)) {
            log.info("Starting registered user for chat: {}", chatId);
            userService.registeredUser(chatId, message.getChat());
        }

        if (update.hasMessage() && update.getMessage().hasText() && !update.hasCallbackQuery()) {
            String messageText = update.getMessage().getText();
            log.info("Handle strategy for command: {}", messageText);
            CommandHandlerStrategy commandHandlerStrategy = commandHandlerStrategies.get(CommandType.fromString(messageText));
            commandHandlerStrategy.handle(this, chatId);
        }

        if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            ButtonCallback buttonCallback = ButtonCallback.fromCallbackData(callbackData);
            MenuType menuType = ButtonCallbackUtils.getMenuType(buttonCallback);
            cityService.selectedCity(callbackData, chatId);
            placeService.selectedPlace(callbackData, chatId);

            log.info("Handle strategy menu for button callback: {} and type menu: {}", buttonCallback, menuType);
            MenuStrategy menuStrategy = menuStrategies.get(menuType);
            menuStrategy.handle(this, update);
        }
    }

}

