package io.project.BorovskBot.service.impl;

import com.vdurmont.emoji.EmojiParser;
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

import static io.project.BorovskBot.service.constants.LogText.METHOD_CALLED;
import static io.project.BorovskBot.service.constants.TelegramText.NOT_FOUND_COMMAND;

@Slf4j
@Service
public class SendingServiceImpl implements io.project.BorovskBot.service.SendingService {

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
     * Отправляет фотографию в чат
     * @param chatId ID чата
     * @param caption текст-описание
     * @return объект {@link SendPhoto}
     * @throws IOException ошибка ввода/вывода
     */
    @Override
    public SendPhoto sendPhoto(Long chatId, String caption) throws IOException {
        log.info(METHOD_CALLED + Thread.currentThread().getStackTrace()[2].getMethodName());
        File imageFile = new ClassPathResource(pathStartPhoto).getFile();
        InputFile imageInputFile = new InputFile().setMedia(imageFile);
        var message = new SendPhoto();
        message.setChatId(chatId);
        message.setCaption(caption);
        message.setPhoto(imageInputFile);
        return message;
    }

}
