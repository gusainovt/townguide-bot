package io.project.BorovskBot.listener;

import io.project.BorovskBot.config.BotConfig;
import io.project.BorovskBot.model.User;
import io.project.BorovskBot.model.dto.AdDto;
import io.project.BorovskBot.service.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

import static io.project.BorovskBot.service.constants.Buttons.*;
import static io.project.BorovskBot.service.constants.Commands.*;
import static io.project.BorovskBot.service.constants.ErrorText.ERROR_SETTING;
import static io.project.BorovskBot.service.constants.LogText.METHOD_CALLED;
import static io.project.BorovskBot.service.constants.TelegramText.HELP_TEXT;


@Component
@Slf4j
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {


    @Autowired
    private final UserService userService;
    @Autowired
    private final BotConfig config;

    @Autowired
    private final SendingService sendingService;

    @Autowired
    private final AdsService adsService;

    @Autowired
    private final StoryService storyService;

    @Autowired
    private final MenuService menuService;

    @Autowired
    private final CallbackService callbackService;

    /**
     * Метод создает меню с командами
     */
    @PostConstruct
    public void initCommands() {
        log.info(METHOD_CALLED + Thread.currentThread().getStackTrace()[2].getMethodName());
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
        log.info(METHOD_CALLED + Thread.currentThread().getStackTrace()[2].getMethodName());
        return config.getBotName();
    }
    @Override
    public String getBotToken() {
        log.info(METHOD_CALLED + Thread.currentThread().getStackTrace()[2].getMethodName());
        return config.getToken();
    }

    /**
     * Метод реализует основную логику взаимодействия с ботом через команды и кнопки.
     * @param update объект {@link Update} из библиотеки телеграмма
     */
    @Override
    @SneakyThrows
    public void onUpdateReceived(Update update) {
        log.info(METHOD_CALLED + Thread.currentThread().getStackTrace()[2].getMethodName());
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            switch (messageText) {
                        case COMMAND_START:
                            execute(sendingService.startCommandReceived(chatId, update.getMessage().getChat().getFirstName()));
                            break;
                        case COMMAND_HELP:
                            execute(sendingService.sendMessage(chatId, HELP_TEXT));
                            break;
                        case COMMAND_REGISTER:
                            execute(menuService.registerMenu(chatId));
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
        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            String levelMenu = callbackData.split("_")[0];
            switch (levelMenu) {
                case LEVEL_REG:
                    execute(callbackService.buttonRegister(update, callbackData));
                    break;
                case LEVEL_START:
                    execute(callbackService.buttonStart(update, callbackData));
                    break;
                case LEVEL_PLACE:
                    execute(callbackService.buttonPlace(update, callbackData));
                    break;
            }

        }
    }




    /**
     * Отправляет объявления пользователям каждый понедельник
     */
    @Scheduled(cron = "${cron.scheduler}")
    @SneakyThrows
    public void sendAds() {
        log.info(METHOD_CALLED + Thread.currentThread().getStackTrace()[2].getMethodName());
        var ads = adsService.findAllAds();
        var users = userService.findAllUsers();

        for (AdDto ad : ads) {
            for (User user : users) {
                execute(sendingService.sendMessage(user.getChatId(), ad.getAd()));
            }
        }
    }

}

