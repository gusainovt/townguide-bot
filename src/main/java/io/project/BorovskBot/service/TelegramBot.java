package io.project.BorovskBot.service;

import io.project.BorovskBot.config.BotConfig;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Objects;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    final BotConfig config;
    public TelegramBot(BotConfig config){
        this.config = config;
    }
    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
            if(update.hasMessage() && update.getMessage().hasText()){
                String messageText = update.getMessage().getText();
                long chatId = update.getMessage().getChatId();
                switch (messageText){
                    case "/start":

                            startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                        break;
                    default:
                            sendMessede(chatId, "Извини, брат, это пока не работает.");

                }
            }
    }
    private void startCommandReceived(long chatId,String name)  {
        String answer = "Привет, " + name + "!\n"
                + "Рад знакомству. Я бот, который познакомит тебя с городом Боровском.\n" +
                "Пока я больше ничего не умею, но скоро научусь.";
        String answerForBarbarossa = name + "! Черт, тебя здесь не ждали! \n" + "Боровск не для предателей Руси!\n";
        if (Objects.equals(name, "Barbarossa")){
            sendMessede(chatId,answerForBarbarossa);
        }
        else {
            sendMessede(chatId, answer);

        }
    }
    private void sendMessede(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        try {
            execute(message);
        }
        catch (TelegramApiException e){

        }
    }
}
