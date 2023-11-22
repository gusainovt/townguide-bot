package io.project.BorovskBot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public interface MenuService {
    InlineKeyboardMarkup startMenu();
    SendMessage registerMenu(long chatId);
    SendMessage placeMenu(SendMessage message);
}
