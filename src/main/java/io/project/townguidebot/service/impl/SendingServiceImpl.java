package io.project.townguidebot.service.impl;

import com.vdurmont.emoji.EmojiParser;
import io.project.townguidebot.model.Weather;
import io.project.townguidebot.service.MenuService;
import io.project.townguidebot.service.PhotoService;
import io.project.townguidebot.service.SendingService;
import io.project.townguidebot.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.File;
import java.io.IOException;

import static io.project.townguidebot.service.constants.LogText.METHOD_CALLED;
import static io.project.townguidebot.service.constants.LogText.REPLIED_USER;
import static io.project.townguidebot.service.constants.TelegramText.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendingServiceImpl implements SendingService {

    private final MenuService menuService;
    private final WeatherService weatherService;
    private final PhotoService photoService;


    @Value("${path.start-photo}")
    private String pathStartPhoto;

    /**
     * Метод изменяет текст сообщения
     * @param text новый текст
     * @param chatId ID чата
     * @param messageId ID сообщения, которое нужно изменить
     * @return объект {@link EditMessageText}
     */
    @Override
    public EditMessageText sendEditMessageText(String text, long chatId, long messageId) {
        log.info(METHOD_CALLED + Thread.currentThread().getStackTrace()[2].getMethodName());
        EditMessageText message = new EditMessageText();
        message.setChatId(chatId);
        message.setText(text);
        message.setMessageId((int) messageId);
        return message;
    }

    /**
     * Отправляет сообщение пользователю
     * @param chatId ID чата
     * @param textToSend текст сообщения
     * @return объект {@link SendMessage}
     */
    @Override
    public SendMessage sendMessage(long chatId, String textToSend) {
        log.info(METHOD_CALLED + Thread.currentThread().getStackTrace()[2].getMethodName());
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);
        return message;
    }

    /**
     * Отправляет сообщение пользователю, что комманда не найдена
     * @param chatId ID чата
     * @return объект {@link SendMessage}
     */
    @Override
    public SendMessage commandNotFound(long chatId) {
        log.info(METHOD_CALLED + Thread.currentThread().getStackTrace()[2].getMethodName());
        return sendMessage(chatId, EmojiParser.parseToUnicode(NOT_FOUND_COMMAND));
    }

    /**
     * Отправляет стартовую фотографию в чат
     * @param chatId ID чата
     * @param caption текст-описание
     * @return объект {@link SendPhoto}
     * @throws IOException ошибка ввода/вывода
     */
    @Override
    public SendPhoto sendStartPhoto(Long chatId, String caption) throws IOException {
        log.info(METHOD_CALLED + Thread.currentThread().getStackTrace()[2].getMethodName());
        File imageFile = new ClassPathResource(pathStartPhoto).getFile();
        InputFile imageInputFile = new InputFile().setMedia(imageFile);
        var message = new SendPhoto();
        message.setChatId(chatId);
        message.setCaption(caption);
        message.setPhoto(imageInputFile);
        return message;
    }

    /**
     * Стартовое приветствие
     * @param chatId ID чата
     * @param name Имя пользователя
     */
    @SneakyThrows
    @Override
    public SendPhoto startCommandReceived(long chatId, String name) {
        log.info(METHOD_CALLED + Thread.currentThread().getStackTrace()[2].getMethodName());
        String answer = EmojiParser.parseToUnicode(HELLO + name + GREETING);
        log.info(REPLIED_USER + name);
        SendPhoto sendPhoto = sendStartPhoto(chatId, answer);
        sendPhoto.setReplyMarkup(menuService.startMenu());
        return sendPhoto;
    }

    /**
     * Отправляет текущую погоду пользователю
     * @param chatId ID чата
     */
    @Override
    public SendMessage sendWeather(long chatId){
        log.info(METHOD_CALLED + Thread.currentThread().getStackTrace()[2].getMethodName());
        Weather weather = weatherService.getWeather(NAME_CITY);
        return sendMessage(chatId,
                String.format(TEXT_WEATHER,
                        weather.getMain().getTemp().toBigInteger(),
                        weather.getMain().getFeels_like().toBigInteger(),
                        weather.getWind().getSpeed().toString()));
    }

    /**
     * Отправляет фотографию в чат
     * @param chatId ID чата
     * @param path путь к фотографии
     * @return объект {@link SendPhoto}
     * @throws IOException ошибка ввода/вывода
     */
    @Override
    public SendPhoto sendPhoto(Long chatId, String path) throws IOException {
        log.info(METHOD_CALLED + Thread.currentThread().getStackTrace()[2].getMethodName());
        File imageFile = new ClassPathResource(path).getFile();
        InputFile imageInputFile = new InputFile().setMedia(imageFile);
        var message = new SendPhoto();
        message.setChatId(chatId);
        message.setPhoto(imageInputFile);
        return message;
    }
}
