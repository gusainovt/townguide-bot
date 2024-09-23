package io.project.townguidebot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public interface MenuService {
    InlineKeyboardMarkup startMenu();
    SendMessage placeMenu(SendMessage message, String cityName);
    InlineKeyboardMarkup cityMenu();
    SendMessage photoMenu(SendMessage message);
}
