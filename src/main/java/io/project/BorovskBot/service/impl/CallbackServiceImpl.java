package io.project.BorovskBot.service.impl;

import io.project.BorovskBot.model.dto.PlaceDto;
import io.project.BorovskBot.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;

import static io.project.BorovskBot.service.constants.Buttons.*;
import static io.project.BorovskBot.service.constants.LogText.METHOD_CALLED;
import static io.project.BorovskBot.service.constants.TelegramText.REGISTER_CANCEL;
import static io.project.BorovskBot.service.constants.TelegramText.REGISTER_CONFIRMATION;

@Service
@Slf4j
@RequiredArgsConstructor
public class CallbackServiceImpl implements io.project.BorovskBot.service.CallbackService {

    private final SendingService sendingService;
    private final UserService userService;
    private final StoryService storyService;
    private final MenuService menuService;
    private final PlaceService placeService;
    private final PhotoService photoService;


    /**
     * Ответ на кнопки регистрации
     * @param update объект {@link Update} из библиотеки телеграмма
     */
    @Override
    public EditMessageText buttonRegister(Update update, String callbackData) {
        log.info(METHOD_CALLED + Thread.currentThread().getStackTrace()[2].getMethodName());
        long messageId = update.getCallbackQuery().getMessage().getMessageId();
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        if (callbackData.equals(YES_CALLBACK)) {
            userService.registeredUser(update.getCallbackQuery().getMessage());
            return sendingService.sendEditMessageText(REGISTER_CONFIRMATION, chatId, messageId);
        } else {
            return sendingService.sendEditMessageText(REGISTER_CANCEL, chatId, messageId);
        }
    }

    /**
     * Ответ на кнопки меню старт
     * @param update бъект {@link Update} из библиотеки телеграмма
     * @param callbackData Ответ от кнопки
     * @return {@link SendMessage}
     */
    @Override
    public SendMessage buttonStart(Update update, String callbackData) {
        log.info(METHOD_CALLED + Thread.currentThread().getStackTrace()[2].getMethodName());
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        switch (callbackData) {
            case STORY_CALLBACK:
                return sendingService.sendMessage(chatId, storyService.getRandomStory().getBody());
            case PLACE_CALLBACK:
                PlaceDto randomPlace = placeService.getRandomStory();
                SendMessage message = sendingService.sendMessage(chatId,
                        randomPlace.getName() +
                                "\n" + randomPlace.getDescription());
                return menuService.placeMenu(message);
        }
        return new SendMessage();
    }

    /**
     * Ответ на кнопки меню места
     * @param update бъект {@link Update} из библиотеки телеграмма
     * @param callbackData Ответ от кнопки
     * @return {@link SendPhoto}
     */
    @Override
    public SendPhoto buttonPlace(Update update, String callbackData) throws IOException {
        log.info(METHOD_CALLED + Thread.currentThread().getStackTrace()[2].getMethodName());
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        if (callbackData.equals(PHOTO_CALLBACK)) {
            return sendingService.sendPhoto(chatId, photoService.getPhotoPathById(14L));
        } else {
            return new SendPhoto();
        }
    }





}
