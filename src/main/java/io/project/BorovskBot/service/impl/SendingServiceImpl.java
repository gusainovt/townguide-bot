package io.project.BorovskBot.service.impl;

import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class SendingServiceImpl implements io.project.BorovskBot.service.SendingService {

    @Override
    public EditMessageText sendEditMessageText(String text, long chatId, long messageId) {
        EditMessageText message = new EditMessageText();
        message.setChatId(chatId);
        message.setText(text);
        message.setMessageId((int) messageId);
        return message;
    }

    @Override
    public SendMessage sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);
        return message;
    }

    @Override
    public SendMessage prepareAndSendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();

        row.add("Погода");
        row.add("Рандомная локация");

        keyboardRows.add(row);

        row = new KeyboardRow();

        row.add("Регистрация");
        row.add("Посмотреть мои данные");
        row.add("Удалить данные");

        keyboardRows.add(row);
        keyboardMarkup.setKeyboard(keyboardRows);
        message.setReplyMarkup(keyboardMarkup);
        return message;

    }


    @Override
    public SendMessage commandNotFound(long chatId) {
        String answer = EmojiParser.parseToUnicode(
                "Извини, это пока не работает."
        );
        return sendMessage(chatId, answer);
    }

    @Override
    public SendPhoto sendPhoto(Long chatId, String caption) throws IOException {
        File imageFile = new ClassPathResource("/media/hello_photo.jpg").getFile();
        InputFile imageInputFile = new InputFile().setMedia(imageFile);
        var message = new SendPhoto();
        message.setChatId(chatId);
        message.setCaption(caption);
        message.setPhoto(imageInputFile);
        return message;
    }

}
