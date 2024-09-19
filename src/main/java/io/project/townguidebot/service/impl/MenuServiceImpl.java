package io.project.townguidebot.service.impl;

import io.project.townguidebot.model.ButtonCallback;
import io.project.townguidebot.model.City;
import io.project.townguidebot.service.CityService;
import io.project.townguidebot.service.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static io.project.townguidebot.model.ButtonCallback.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final CityService cityService;

    /**
     * Вызов стартового меню
     * @return объект {@link InlineKeyboardMarkup}
     */
    @Override
    public InlineKeyboardMarkup startMenu() {
        log.info("Generating start menu...");
        List<City> cities = cityService.getAllCity();
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();

        for (City city : cities) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(city.getName());
            button.setCallbackData(ButtonCallback.CITY.name() + ":" + city.getNameEng());
            rowsInLine.add(List.of(button));
        }

        markupInline.setKeyboard(rowsInLine);
        return markupInline;
    }


    /**
     * Меню места
     * @param message сообщение с местом
     */
    @Override
    public SendMessage placeMenu(SendMessage message) {
        log.info("Activate place menu");
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        var photoButton = new InlineKeyboardButton();

        photoButton.setText("PLACE_PHOTO_BUTTON");
        photoButton.setCallbackData(PHOTO.toString());

        rowInLine.add(photoButton);
        rowsInLine.add(rowInLine);

        markupInline.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInline);
        return message;
    }

    /**
     * Вызов стартового меню
     * @return объект {@link InlineKeyboardMarkup}
     */
    @Override
    public InlineKeyboardMarkup cityMenu() {

        log.info("Generating city menu...");
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        var getStory = new InlineKeyboardButton();

        getStory.setText("История");
        getStory.setCallbackData(STORY.toString());

        var getPhoto = new InlineKeyboardButton();

        getPhoto.setText("Место");
        getPhoto.setCallbackData(PLACE.toString());

        rowInLine.add(getStory);
        rowInLine.add(getPhoto);
        rowsInLine.add(rowInLine);

        markupInline.setKeyboard(rowsInLine);

        return markupInline;
    }
}
