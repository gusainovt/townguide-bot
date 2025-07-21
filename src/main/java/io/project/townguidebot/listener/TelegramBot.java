package io.project.townguidebot.listener;

import io.project.townguidebot.config.BotConfig;
import io.project.townguidebot.model.MenuType;
import io.project.townguidebot.service.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static io.project.townguidebot.service.constants.Commands.*;
import static io.project.townguidebot.service.constants.ErrorText.ERROR_SETTING;
import static io.project.townguidebot.service.constants.TelegramText.HELP_TEXT;


@Component
@Slf4j
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {


    private final BotConfig config;
    private final SendingService sendingService;
    private final AdsService adsService;
    private final UserService userService;
    private final StoryService storyService;
    private final MenuService menuService;
    private final CallbackService callbackService;

    /**
     * Метод создает меню с командами
     */
    @PostConstruct
    public void initCommands() {
        log.info("Initialization bot menu");
        List<BotCommand> listOfCommands = new ArrayList<>(List.of(
                new BotCommand(COMMAND_START, DESCRIPTION_START),
                new BotCommand(COMMAND_MY_DATA, DESCRIPTION_MY_DATA),
                new BotCommand(COMMAND_DELETE_DATA, DESCRIPTION_DELETE_DATA),
                new BotCommand(COMMAND_HELP, DESCRIPTION_HELP),
                new BotCommand(COMMAND_SETTING, DESCRIPTION_SETTING),
                new BotCommand(COMMAND_REGISTER, DESCRIPTION_REGISTER),
                new BotCommand(COMMAND_STORY, DESCRIPTION_JOKE),
                new BotCommand(COMMAND_WEATHER, DESCRIPTION_WEATHER)
        ));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));

        } catch (TelegramApiException e) {
            log.error(ERROR_SETTING + e.getMessage());
        }

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

        if (!userService.isRegisteredUser(chatId) && !update.hasCallbackQuery()) {
            execute(menuService.registerMenu(chatId));
            return;
        }
        if (!userService.isRegisteredUser(chatId) && update.hasCallbackQuery()) {
            execute(callbackService.buttonRegister(update));
        }



        if (update.hasMessage() && update.getMessage().hasText() && !update.hasCallbackQuery()) {
            String messageText = update.getMessage().getText();

            switch (messageText) {
                        case COMMAND_START:
                            execute(sendingService.startCommandReceived(chatId, update.getMessage().getChat().getFirstName()));
                            break;
                        case COMMAND_HELP:
                            execute(sendingService.sendMessage(chatId, HELP_TEXT));
                            break;
                        case COMMAND_REGISTER:
                            break;
                        case COMMAND_STORY:
                            execute(sendingService.sendMessage(chatId, storyService.getRandomStory().getBody()));
                            break;
                        case COMMAND_WEATHER:
                            execute(sendingService.sendWeather(chatId));
                            break;
                        default:
                            execute(sendingService.commandNotFound(chatId));
            }
        }

        if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();

            MenuType levelMenu = MenuType.valueOf(callbackData.split("_")[0]);

            switch (levelMenu) {
                case REG:
                    execute(callbackService.buttonRegister(update));
                    break;
                case START:
                    execute(callbackService.buttonStart(update, callbackData));
                    break;
                case PLACE:
                    execute(callbackService.buttonPlace(update, callbackData));
                    break;
            }

        }
    }





}

