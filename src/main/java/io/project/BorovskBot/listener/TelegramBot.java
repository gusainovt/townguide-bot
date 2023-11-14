package io.project.BorovskBot.listener;

import com.vdurmont.emoji.EmojiParser;
import io.project.BorovskBot.config.BotConfig;
import io.project.BorovskBot.model.Ads;
import io.project.BorovskBot.model.User;
import io.project.BorovskBot.model.Weather;
import io.project.BorovskBot.service.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

import static io.project.BorovskBot.service.constants.Commands.*;
import static io.project.BorovskBot.service.constants.ErrorText.ERROR_SETTING;
import static io.project.BorovskBot.service.constants.LogText.REPLIED_USER;
import static io.project.BorovskBot.service.constants.TelegramText.*;


@Component
@Slf4j
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {


    private final UserService userService;
    private final BotConfig config;
    private final PlaceService placeService;
    private final SendingService sendingService;
    private final AdsService adsService;
    private final PhotoService photoService;
    private final WeatherService weatherService;
    private final JokeService jokeService;

    /**
     * Метод создает меню с командами
     */
    @PostConstruct
    public void initCommands() {
        List<BotCommand> listOfCommands = new ArrayList<>(List.of(
                new BotCommand(COMMAND_START, DESCRIPTION_START),
                new BotCommand(COMMAND_MY_DATA, DESCRIPTION_MY_DATA),
                new BotCommand(COMMAND_DELETE_DATA, DESCRIPTION_DELETE_DATA),
                new BotCommand(COMMAND_HELP, DESCRIPTION_HELP),
                new BotCommand(COMMAND_SETTING, DESCRIPTION_SETTING),
                new BotCommand(COMMAND_REGISTER, DESCRIPTION_REGISTER),
                new BotCommand(COMMAND_JOKE, DESCRIPTION_JOKE),
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
        return config.getBotName();
    }
    @Override
    public String getBotToken() {
        return config.getToken();
    }

    /**
     * Метод реализует основную логику взаимодействия с ботом через команды и кнопки.
     * @param update объект {@link Update} из библиотеки телеграмма
     */
    @Override
    @SneakyThrows
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            switch (messageText) {
                        case COMMAND_START:
                            startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                            break;
                        case COMMAND_HELP:
                            execute(sendingService.sendMessage(chatId, HELP_TEXT));
                            break;
                        case COMMAND_REGISTER:
                            register(chatId);
                            break;
                        case COMMAND_JOKE:
                            execute(sendingService.sendMessage(chatId, jokeService.getRandomJoke().getBody()));
                            break;
                        case COMMAND_WEATHER:
                            Weather weather = weatherService.getWeather(NAME_CITY);
                            execute(sendingService.sendMessage(chatId, getTextWeather(weather)));
                        default:
                            execute(sendingService.commandNotFound(chatId));
            }

        } else if (update.hasCallbackQuery()) {
            buttonRegister(update);
        }
    }

    /**
     * Стартовое приветствие
     * @param chatId ID чата
     * @param name Имя пользователя
     */
    @SneakyThrows
    private void startCommandReceived(long chatId, String name) {
        String answer = EmojiParser.parseToUnicode(HELLO + name + GREETING);
        log.info(REPLIED_USER + name);
        execute(sendingService.sendPhoto(chatId, answer));
    }

    /**
     * Регистрация пользователя
     * @param chatId ID чата
     */
    @SneakyThrows
    private void register(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(REGISTER_QUESTION);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        var yesButton = new InlineKeyboardButton();

        yesButton.setText(YES);
        yesButton.setCallbackData(YES_BUTTON);

        var noButton = new InlineKeyboardButton();

        noButton.setText(NO);
        noButton.setCallbackData(NO_BUTTON);

        rowInLine.add(yesButton);
        rowInLine.add(noButton);

        rowsInLine.add(rowInLine);

        markupInline.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInline);
        execute(message);
    }

    /**
     * Меню регистрации (кнопки)
     * @param update объект {@link Update} из библиотеки телеграмма
     */
    @SneakyThrows
    private void buttonRegister(Update update) {
        String callbackData = update.getCallbackQuery().getData();
        long messageId = update.getCallbackQuery().getMessage().getMessageId();
        long chatId = update.getCallbackQuery().getMessage().getChatId();

        if (callbackData.equals(YES_BUTTON)) {
            userService.registeredUser(update.getMessage());
            execute(sendingService.sendEditMessageText(REGISTER_CONFIRMATION, chatId, messageId));
        } else if (callbackData.equals(NO_BUTTON)) {
            execute(sendingService.sendEditMessageText(REGISTER_CANCEL, chatId, messageId));
        }

    }

    /**
     * Отправляет объявления пользователям каждый понедельник
     */
    @Scheduled(cron = "${cron.scheduler}")
    @SneakyThrows
    public void sendAds() {
        var ads = adsService.findAllAds();
        var users = userService.findAllUsers();

        for (Ads ad : ads) {
            for (User user : users) {
                execute(sendingService.sendMessage(user.getChatId(), ad.getAd()));
            }
        }
    }

}

