package io.project.townguidebot.service.impl;

import io.project.townguidebot.model.ButtonCallback;
import io.project.townguidebot.service.*;
import io.project.townguidebot.service.strategy.CallbackSendMessageStrategy;
import io.project.townguidebot.service.strategy.RegisterStrategy;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.project.townguidebot.model.ButtonCallback.PHOTO;

@Service
@Slf4j
@RequiredArgsConstructor
public class CallbackServiceImpl implements CallbackService {

    private final SendingService sendingService;
    private final PhotoService photoService;

    private final List<RegisterStrategy> registerStrategyList;
    private Map<ButtonCallback, RegisterStrategy> registerStrategies;

    private final List<CallbackSendMessageStrategy> callbackSendMessageStrategyList;
    private Map<ButtonCallback, CallbackSendMessageStrategy> callbackSendMessageStrategies;


    @PostConstruct
    public void init() {
        registerStrategies = registerStrategyList.stream()
                .collect(Collectors.toMap(RegisterStrategy::getButtonCallback, s -> s));

        callbackSendMessageStrategies = callbackSendMessageStrategyList.stream()
                .collect(Collectors.toMap(CallbackSendMessageStrategy::getButtonCallback, s -> s));
    }

    /**
     * Ответ на кнопки регистрации
     *
     * @param update {@link Update} из библиотеки телеграмма
     */
    @Override
    public EditMessageText buttonRegister(Update update) {
        ButtonCallback callback = ButtonCallback.valueOf(update.getCallbackQuery().getData());
        Message message = update.getCallbackQuery().getMessage();

        log.info("Activate buttons registered for chat: {} and callback: {}", message.getChatId(), callback);
        RegisterStrategy registerStrategy = registerStrategies.get(callback);
        return registerStrategy.handle(message);
    }

    /**
     * Ответ на кнопки меню старт
     * @param update объект {@link Update} из библиотеки телеграмма
     * @return {@link SendMessage}
     */
    @Override
    public SendMessage buttonStart(Update update) {
        Message message = update.getCallbackQuery().getMessage();
        ButtonCallback callback = ButtonCallback.valueOf(update.getCallbackQuery().getData());

        log.info("Activate buttons in start menu for chat: {} and callback: {}", message.getChatId(), callback);

        CallbackSendMessageStrategy callbackSendMessageStrategy = callbackSendMessageStrategies.get(callback);
        return callbackSendMessageStrategy.handle(message);

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
