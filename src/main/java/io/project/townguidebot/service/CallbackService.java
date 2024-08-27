package io.project.townguidebot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface CallbackService {
    SendMessage buttonStart(Update update);
    SendPhoto buttonPlace(Update update);

}
