package io.project.BorovskBot.service.impl;

import io.project.BorovskBot.service.SendingService;
import io.project.BorovskBot.service.StoryService;
import io.project.BorovskBot.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

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

    /**
     * Ответ на кнопки регистрации
     * @param update объект {@link Update} из библиотеки телеграмма
     */
    @SneakyThrows
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

    @Override
    public SendMessage buttonStart(Update update, String callbackData) {
        log.info(METHOD_CALLED + Thread.currentThread().getStackTrace()[2].getMethodName());
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        switch (callbackData) {
            case STORY_CALLBACK:
                return sendingService.sendMessage(chatId, storyService.getRandomStory().getBody());
            case PHOTO_CALLBACK:
                break;
        }
        return new SendMessage();
    }

}
