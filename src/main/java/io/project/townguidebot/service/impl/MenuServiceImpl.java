package io.project.townguidebot.service.impl;

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
import static io.project.townguidebot.service.constants.TelegramText.REGISTER_QUESTION;

@Service
@Slf4j
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {


    /**
     * Вызов стартового меню
     * @return объект {@link InlineKeyboardMarkup}
     */
    @Override
    public InlineKeyboardMarkup startMenu() {
        log.info("Generating start menu...");
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        var getStory = new InlineKeyboardButton();

        getStory.setText("START_STORY_BUTTON");
        getStory.setCallbackData(STORY.toString());

        var getPhoto = new InlineKeyboardButton();

        getPhoto.setText("START_PLACE_BUTTON");
        getPhoto.setCallbackData(PLACE.toString());

        rowInLine.add(getStory);
        rowInLine.add(getPhoto);

        rowsInLine.add(rowInLine);

        markupInline.setKeyboard(rowsInLine);
        return markupInline;
    }

    /**
     * Регистрация пользователя
     * @param chatId ID чата
     */
    @Override
    public SendMessage registerMenu(long chatId) {
        log.info("Register menu culled in chat: {}", chatId);
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(REGISTER_QUESTION);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        var russianButton = new InlineKeyboardButton();

        russianButton.setText("Русский");
        russianButton.setCallbackData(LANGUAGE_CODE_RU.toString());

        var cancelButton = new InlineKeyboardButton();

        cancelButton.setText("Cancel");
        cancelButton.setCallbackData(CANCEL.toString());

        rowInLine.add(russianButton);
        rowInLine.add(cancelButton);

        rowsInLine.add(rowInLine);

        markupInline.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInline);
        return message;
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
}
