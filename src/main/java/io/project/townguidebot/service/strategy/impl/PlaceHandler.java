package io.project.townguidebot.service.strategy.impl;

import io.project.townguidebot.model.ButtonCallback;
import io.project.townguidebot.model.dto.PlaceDto;
import io.project.townguidebot.service.MenuService;
import io.project.townguidebot.service.PlaceService;
import io.project.townguidebot.service.SendingService;
import io.project.townguidebot.service.strategy.CallbackSendMessageStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@RequiredArgsConstructor
public class PlaceHandler implements CallbackSendMessageStrategy {

    private final ButtonCallback buttonCallback = ButtonCallback.PLACE;
    private final SendingService sendingService;
    private final PlaceService placeService;
    private final MenuService menuService;


    @Override
    public ButtonCallback getButtonCallback() {
        return buttonCallback;
    }

    @Override
    public SendMessage handle(String cityName, Message message) {
        long chatId = message.getChatId();
        PlaceDto randomPlace = placeService.getRandomStory();
        SendMessage sendMessage = sendingService.sendMessage(chatId,
                randomPlace.getName() +
                        "\n" + randomPlace.getDescription());
        return menuService.placeMenu(sendMessage);
    }
}
