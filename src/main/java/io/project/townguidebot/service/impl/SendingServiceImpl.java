package io.project.townguidebot.service.impl;

import com.vdurmont.emoji.EmojiParser;
import io.project.townguidebot.model.Weather;
import io.project.townguidebot.service.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.File;
import java.io.IOException;

import static io.project.townguidebot.service.constants.TelegramText.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendingServiceImpl implements SendingService {

    private final MenuService menuService;
    private final WeatherService weatherService;
    private final UserService userService;
    private final StoryService storyService;
    private final CityService cityService;

    /**
     * Метод изменяет текст сообщения
     * @param text новый текст
     * @param chatId ID чата
     * @param messageId ID сообщения, которое нужно изменить
     * @return объект {@link EditMessageText}
     */
    @Override
    public EditMessageText sendEditMessageText(String text, Long chatId, Long messageId) {
        log.info("Edit message: {} for chat: {}", messageId, chatId);
        EditMessageText message = new EditMessageText();
        message.setChatId(chatId);
        message.setText(text);
        message.setMessageId(messageId.intValue());
        return message;
    }

    /**
     * Отправляет сообщение пользователю
     * @param chatId ID чата
     * @param textToSend текст сообщения
     * @return объект {@link SendMessage}
     */
    @Override
    public SendMessage sendMessage(Long chatId, String textToSend) {
        log.info("Sending message for chat: {}", chatId);
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);
        return message;
    }

    /**
     * Отправляет сообщение пользователю, что команда не найдена
     * @param chatId ID чата
     * @return объект {@link SendMessage}
     */
    @Override
    public SendMessage commandNotFound(Long chatId) {
        log.info("Command not found for chat: {}", chatId);
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
        log.info("Sending start photo for chat: {}", chatId);
        String photoUrl = "https://res.cloudinary.com/dezwmxtpc/image/upload/v1756196617/places/1/zopw1n5x3wobenrgwuc3.png";
        var message = new SendPhoto();
        message.setChatId(chatId);
        message.setCaption(caption);
        message.setPhoto(new InputFile(photoUrl));
        return message;
    }

    /**
     * Стартовое приветствие
     * @param chatId ID чата
     */
    @SneakyThrows
    @Override
    public SendPhoto startCommandReceived(Long chatId) {
        log.info("Hello message for chat: {}", chatId);
        cityService.unselectedCityForChat(chatId);
        String name = userService.getNameByChatId(chatId);
        String answer = EmojiParser.parseToUnicode(HELLO + name + GREETING);
        SendPhoto sendPhoto = sendStartPhoto(chatId, answer);
        sendPhoto.setReplyMarkup(menuService.startMenu());
        return sendPhoto;
    }

    /**
     * Отправляет текущую погоду пользователю
     * @param chatId ID чата
     */
    @Override
    public SendMessage sendWeather(Long chatId){
        String cityName = cityService.getSelectedCityForChat(chatId);
        log.info("Sending weather for chat: {} and city: {}", chatId, cityName);
        Weather weather = weatherService.getWeather(cityName);
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
        log.info("Sending photo for chat: {}", chatId);
        File imageFile = new ClassPathResource(path).getFile();
        InputFile imageInputFile = new InputFile().setMedia(imageFile);
        var message = new SendPhoto();
        message.setChatId(chatId);
        message.setPhoto(imageInputFile);
        return message;
    }

    /**
     * Отправляет рандомную историю пользователю
     * @param chatId id чата
     * @return объект {@link SendMessage}
     */
    @Override
    public SendMessage sendRandomStory(Long chatId) {
        log.info("Sending random story for chat: {}", chatId);
        String storyText = storyService.getRandomStoryForCity(chatId).getBody();
        return sendMessage(chatId, storyText);
    }

    /**
     * Отправляет текс с руководством
     * @param chatId id чата
     * @return объект {@link SendMessage}
     */
    @Override
    public SendMessage sendHelpText(Long chatId) {
        return sendMessage(chatId, HELP_TEXT);
    }

    /**
     * Стартовое меню города
     * @param chatId ID чата
     */
    @SneakyThrows
    @Override
    public SendMessage cityMenuReceived(Long chatId) {
        log.info("City menu for chat: {}", chatId);
        String descriptionCity = cityService.getDescriptionSelectedCity(chatId);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(descriptionCity);
        sendMessage.setReplyMarkup(menuService.cityMenu());
        return sendMessage;
    }
}
