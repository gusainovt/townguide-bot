package io.project.townguidebot.service.impl;

import io.project.townguidebot.model.ButtonCallback;
import io.project.townguidebot.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;

import static io.project.townguidebot.model.LanguageCode.RU;
import static io.project.townguidebot.service.constants.TelegramText.REGISTER_CANCEL;
import static io.project.townguidebot.service.constants.TelegramText.REGISTER_CONFIRMATION;

@Service
@Slf4j
@RequiredArgsConstructor
public class CallbackServiceImpl implements CallbackService {

    private final SendingService sendingService;
    private final UserService userService;
    private final StoryService storyService;
    private final MenuService menuService;
    private final PlaceService placeService;
    private final PhotoService photoService;


    /**
     * Ответ на кнопки регистрации
     *
     * @param update {@link Update} из библиотеки телеграмма
     */
    @Override
    public EditMessageText buttonRegister(Update update) {
        ButtonCallback callback = ButtonCallback.valueOf(update.getCallbackQuery().getData());
        Message message = update.getCallbackQuery().getMessage();
        long messageId = message.getMessageId();
        long chatId = message.getChatId();

        log.info("Request for button registered for chat: {} and callback: {}", chatId, callback);
        switch (callback) {
            case RLC_RU:
                userService.registeredUser(message, RU);
                return sendingService.sendEditMessageText(REGISTER_CONFIRMATION, chatId, messageId);
            case RCC_CANCEL:
                return sendingService.sendEditMessageText(REGISTER_CANCEL, chatId, messageId);
            default:
                return new EditMessageText();
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
//        log.info(METHOD_CALLED + Thread.currentThread().getStackTrace()[2].getMethodName());
//        long chatId = update.getCallbackQuery().getMessage().getChatId();
//        switch (callbackData) {
//            case STORY_CALLBACK:
//                return sendingService.sendMessage(chatId, storyService.getRandomStory().getBody());
//            case PLACE_CALLBACK:
//                PlaceDto randomPlace = placeService.getRandomStory();
//                SendMessage message = sendingService.sendMessage(chatId,
//                        randomPlace.getName() +
//                                "\n" + randomPlace.getDescription());
//                return menuService.placeMenu(message);
//        }
       return new SendMessage();
    }

    /**
     * Ответ на кнопки меню места
     * @param update {@link Update} из библиотеки телеграмма
     * @param callbackData Ответ от кнопки
     * @return {@link SendPhoto}
     */
    @Override
    public SendPhoto buttonPlace(Update update, String callbackData) throws IOException {
//        log.info(METHOD_CALLED + Thread.currentThread().getStackTrace()[2].getMethodName());
//        long chatId = update.getCallbackQuery().getMessage().getChatId();
//        if (callbackData.equals(PHOTO_CALLBACK)) {
//            return sendingService.sendPhoto(chatId, photoService.getPhotoPathById(14L));
//        } else {
//            return new SendPhoto();
//        }
      return new SendPhoto();
    }

}
