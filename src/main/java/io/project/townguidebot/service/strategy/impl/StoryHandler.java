package io.project.townguidebot.service.strategy.impl;

import io.project.townguidebot.model.ButtonCallback;
import io.project.townguidebot.service.SendingService;
import io.project.townguidebot.service.StoryService;
import io.project.townguidebot.service.strategy.CallbackSendMessageStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@RequiredArgsConstructor
public class StoryHandler implements CallbackSendMessageStrategy {

    private final ButtonCallback buttonCallback = ButtonCallback.STORY;
    private final SendingService sendingService;
    private final StoryService storyService;


    @Override
    public ButtonCallback getButtonCallback() {
        return buttonCallback;
    }

    @Override
    public SendMessage handle(String cityName, Message message) {
        long chatId = message.getChatId();
        return sendingService.sendMessage(chatId, storyService.getRandomStoryForCity(chatId).getBody());
    }
}
