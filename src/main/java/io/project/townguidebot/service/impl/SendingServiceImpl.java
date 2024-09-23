package io.project.townguidebot.service.impl;

import com.vdurmont.emoji.EmojiParser;
import io.project.townguidebot.model.Weather;
import io.project.townguidebot.model.dto.PlaceDto;
import io.project.townguidebot.service.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.IOException;
import java.util.Optional;

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
    private final PlaceService placeService;

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
        log.warn("Command not found for chat: {}", chatId);
        return sendMessage(chatId, EmojiParser.parseToUnicode(NOT_FOUND_COMMAND));
    }

    /**
     * Отправляет сообщение пользователю, что город не выбран
     * @param chatId ID чата
     * @return объект {@link SendMessage}
     */
    @Override
    public SendMessage cityNotSelected(Long chatId) {
        log.warn("City not select for chat: {}", chatId);
        return sendMessage(chatId, EmojiParser.parseToUnicode(CITY_UNSELECTED));
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
        return Optional.ofNullable(cityService.getSelectedCityForChat(chatId))
                .map(cityNameEng -> {
                    log.info("Sending weather for chat: {} and city: {}", chatId, cityNameEng);
                    Weather weather = weatherService.getWeather(cityNameEng);
                    String nameCity = cityService.getCityNameByNameEng(cityNameEng);
                    return sendMessage(chatId,
                            String.format(TEXT_WEATHER,
                                    nameCity,
                                    weather.getMain().getTemp().toBigInteger(),
                                    weather.getMain().getFeels_like().toBigInteger(),
                                    weather.getWind().getSpeed().toString()));
                })
                .orElseGet(()-> cityNotSelected(chatId));
    }

    /**
     * Отправляет фотографию в чат
     * @param chatId ID чата
     * @param urlPhoto путь к фотографии
     * @return объект {@link SendPhoto}
     * @throws IOException ошибка ввода/вывода
     */
    @Override
    public SendPhoto sendPhoto(Long chatId, String urlPhoto) throws IOException {
        log.info("Sending photo for chat: {}", chatId);
        var message = new SendPhoto();
        message.setChatId(chatId);
        message.setPhoto(new InputFile(urlPhoto));
        return message;
    }

    /**
     * Отправляет рандомную историю пользователю
     * @param chatId id чата
     * @return объект {@link SendMessage}
     */
    @Override
    public SendMessage sendRandomStory(Long chatId) {
        return Optional.ofNullable(cityService.getSelectedCityForChat(chatId))
                        .map(cityName -> {
                            log.info("Sending random story for chat: {}", chatId);
                            String storyText = storyService.getRandomStoryForCity(cityName).getBody();
                            SendMessage sendMessage = new SendMessage();
                            sendMessage.setChatId(chatId);
                            sendMessage.setText(storyText);
                            sendMessage.setReplyMarkup(menuService.cityMenu());
                            return sendMessage;
                        })
                        .orElseGet(()-> cityNotSelected(chatId));
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

    /**
     * Меню выбора города
     * @param chatId ID чата
     */
    @SneakyThrows
    @Override
    public SendMessage selectCityCommandReceived(Long chatId) {
        log.info("Menu fo selected city: {}", chatId);
        cityService.unselectedCityForChat(chatId);
        SendMessage sendPhoto = sendMessage(chatId, SELECT_CITY);
        sendPhoto.setReplyMarkup(menuService.startMenu());
        return sendPhoto;
    }

    /**
     * Отправляет рандомное место пользователю
     * @param chatId id чата
     * @return объект {@link SendMessage}
     */
    @Override
    public SendMessage sendRandomPlace(Long chatId) {
        return Optional.ofNullable(cityService.getSelectedCityForChat(chatId))
                .map(cityName->{
                    PlaceDto randomPlace = placeService.getRandomPlaceByCity(cityName);
                    SendMessage sendMessage = sendMessage(chatId,
                            randomPlace.getName() +
                                    "\n" + randomPlace.getDescription());
                    return menuService.placeMenu(sendMessage);
                })
                .orElseGet(()-> cityNotSelected(chatId));
    }

}
