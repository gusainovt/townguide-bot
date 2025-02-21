package io.project.BorovskBot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static io.project.BorovskBot.service.constants.Buttons.*;
import static io.project.BorovskBot.service.constants.LogText.METHOD_CALLED;
import static io.project.BorovskBot.service.constants.TelegramText.REGISTER_QUESTION;

@Service
@Slf4j
@RequiredArgsConstructor
public class MenuServiceImpl implements io.project.BorovskBot.service.MenuService {



    /**
     * Релизация стартового меню
     * @return объект {@link InlineKeyboardMarkup}
     */
    @Override
    public InlineKeyboardMarkup startMenu() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        var getStory = new InlineKeyboardButton();

        getStory.setText(START_STORY_BUTTON);
        getStory.setCallbackData(STORY_CALLBACK);

        var getPhoto = new InlineKeyboardButton();

        getPhoto.setText(START_PLACE_BUTTON);
        getPhoto.setCallbackData(PLACE_CALLBACK);

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
        log.info(METHOD_CALLED + Thread.currentThread().getStackTrace()[2].getMethodName());
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(REGISTER_QUESTION);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        var yesButton = new InlineKeyboardButton();

        yesButton.setText(YES_BUTTON);
        yesButton.setCallbackData(YES_CALLBACK);

        var noButton = new InlineKeyboardButton();

        noButton.setText(NO_BUTTON);
        noButton.setCallbackData(NO_CALLBACK);

        rowInLine.add(yesButton);
        rowInLine.add(noButton);

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
        log.info(METHOD_CALLED + Thread.currentThread().getStackTrace()[2].getMethodName());
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        var photoButton = new InlineKeyboardButton();

        photoButton.setText(PLACE_PHOTO_BUTTON);
        photoButton.setCallbackData(PHOTO_CALLBACK);

        rowInLine.add(photoButton);
        rowsInLine.add(rowInLine);

        markupInline.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInline);
        return message;
    }
}
