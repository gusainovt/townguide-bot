package io.project.BorovskBot.listener;

import com.vdurmont.emoji.EmojiParser;
import io.project.BorovskBot.config.BotConfig;
import io.project.BorovskBot.model.Ads;
import io.project.BorovskBot.model.User;
import io.project.BorovskBot.model.Weather;
import io.project.BorovskBot.service.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static io.project.BorovskBot.service.constants.Commands.*;
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

    List<BotCommand> listOfCommands = new ArrayList<>(List.of(
            new BotCommand(COMMAND_START, "запустить бота"),
            new BotCommand(COMMAND_MY_DATA, "посмотреть свои данные"),
            new BotCommand(COMMAND_DELETE_DATA, "удалить свои данные"),
            new BotCommand(COMMAND_HELP, "информация как пользоваться этим ботом"),
            new BotCommand(COMMAND_SETTING, "настройки"),
            new BotCommand(COMMAND_REGISTER, "регистрация"),
            new BotCommand(COMMAND_JOKE, "рандомная шутка"),
            new BotCommand(COMMAND_WEATHER, "погода в Боровске")
    ));

//    try {
//            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
//
//    } catch (TelegramApiException e) {
////            log.error(ERROR_SETTING + e.getMessage());
//    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }
    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    @SneakyThrows
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            switch (messageText) {
                        case COMMAND_START:
                            userService.registeredUser(update.getMessage());
                            log.info("Registered user: " + update.getMessage().getChat().getUserName());
                            startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                            photoService.uploadPhoto();
                            byte[] bytes = photoRepository.findById(2L).get().getData();
                            BufferedImage image = null;

                            try {
                                image = convertBytesToImage(bytes);
                            } catch (IOException e) {
                                // Обработка ошибки
                            }
                            InputFile imageInputFile = new InputFile();
                            imageInputFile.setMedia(image.getData());
                            var message = new SendPhoto();
                            message.setChatId(chatId);
                            message.setCaption("Новое фото");
                            message.setPhoto(imageInputFile);

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
                            Weather weather = weatherService.getWeather("borovsk");
                            String weatherBorovsk =
                                    "Погода в боровске: \n" +
                                            "Температура - " + weather.getMain().getTemp().toBigInteger() + " градусов; \n" +
                                            "Ошущается как - " + weather.getMain().getFeels_like().toBigInteger() + " градусов; \n" +
                                            "Скорость ветра - " + weather.getWind().getSpeed() + " м/с.";
                            execute(sendingService.sendMessage(chatId, weatherBorovsk));

                        default:
                            sendingService.commandNotFound(chatId);
            }

        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            if (callbackData.equals(YES_BUTTON)) {
                String text = "Ты зарегистррирован!";
                execute(sendingService.sendEditMessageText(text, chatId, messageId));

            } else if (callbackData.equals(NO_BUTTON)) {
                String text = "Отмена регистрации...";
                execute(sendingService.sendEditMessageText(text, chatId, messageId));
            }

        }
    }
    }
    @SneakyThrows
    private void startCommandReceived(long chatId, String name) {
        String answer = EmojiParser.parseToUnicode(HELLO + name + GREETING);
//        log.info(REPLIED_USER + name);
        execute(sendingService.sendPhoto(chatId,answer));
    }

    @SneakyThrows
    private void register(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Ты хочешь зарегистрироваться?");

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        var yesButton = new InlineKeyboardButton();

        yesButton.setText("Да");
        yesButton.setCallbackData(YES_BUTTON);

        var noButton = new InlineKeyboardButton();

        noButton.setText("Нет");
        noButton.setCallbackData(NO_BUTTON);

        rowInLine.add(yesButton);
        rowInLine.add(noButton);

        rowsInLine.add(rowInLine);

        markupInline.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInline);
        execute(message);
    }
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

    @SneakyThrows
    public static BufferedImage convertBytesToImage(byte[] bytes)  {
        BufferedImage image = null;

        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes)) {
            image = ImageIO.read(bis);
        } catch (IOException e) {
            throw new IOException("Error converting bytes to image", e);
        }

        return image;
    }


}

