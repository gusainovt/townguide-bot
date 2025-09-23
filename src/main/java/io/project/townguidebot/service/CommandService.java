package io.project.townguidebot.service;

import io.project.townguidebot.listener.TelegramBot;

public interface CommandService {

    void initCommands(TelegramBot bot);
}
