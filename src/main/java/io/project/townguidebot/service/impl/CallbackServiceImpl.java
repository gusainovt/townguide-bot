package io.project.townguidebot.service.impl;

import io.project.townguidebot.model.ButtonCallback;
import io.project.townguidebot.model.dto.PlaceDto;
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

import static io.project.townguidebot.model.ButtonCallback.*;
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

        log.info("Activate buttons registered for chat: {} and callback: {}", chatId, callback);
        switch (callback) {
            case LANGUAGE_CODE_RU:
                userService.registeredUser(message, RU);
                return sendingService.sendEditMessageText(REGISTER_CONFIRMATION, chatId, messageId);
            case CANCEL:
                return sendingService.sendEditMessageText(REGISTER_CANCEL, chatId, messageId);
            default:
                return new EditMessageText();
        }
    }

    /**
     * Ответ на кнопки меню старт
     * @param update объект {@link Update} из библиотеки телеграмма
     * @return {@link SendMessage}
     */
    @Override
    public SendMessage buttonStart(Update update) {

        long chatId = update.getCallbackQuery().getMessage().getChatId();
        ButtonCallback callback = ButtonCallback.valueOf(update.getCallbackQuery().getData());

        log.info("Activate buttons in start menu for chat: {} and callback: {}", chatId, callback);
        switch (callback) {
            case STORY:
                return sendingService.sendMessage(chatId, storyService.getRandomStory().getBody());
            case PLACE:
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
     * @param update {@link Update} из библиотеки телеграмма
     * @return {@link SendPhoto}
     */
    @Override
    public SendPhoto buttonPlace(Update update) {

        ButtonCallback callback = ButtonCallback.valueOf(update.getCallbackQuery().getData());
        long chatId = update.getCallbackQuery().getMessage().getChatId();

        log.info("Activate buttons in place menu for chat: {} and callback: {}", chatId, callback);

        if (callback.equals(PHOTO)) {
            try {
                return sendingService.sendPhoto(chatId, photoService.getPhotoPathById(14L));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return new SendPhoto();
    }

}
