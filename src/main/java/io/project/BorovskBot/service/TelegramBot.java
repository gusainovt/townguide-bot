package io.project.BorovskBot.service;

import com.vdurmont.emoji.EmojiParser;
import io.project.BorovskBot.config.BotConfig;
import io.project.BorovskBot.model.Ads;
import io.project.BorovskBot.model.User;
import io.project.BorovskBot.repository.AdsRepository;
import io.project.BorovskBot.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdsRepository adsRepository;
    final BotConfig config;
    static final String YES_BUTTON = "YES_BUTTON";
    static final String NO_BUTTON = "NO_BUTTON";
    static final String ERROR_TEXT = "Error occurred: ";
    static final String COMMAND_START = "/start";
    static final String COMMAND_MY_DATA = "/mydata";
    static final String COMMAND_DELETE_DATA = "/deletedata";
    static final String COMMAND_HELP = "/help";
    static final String COMMAND_SETTING = "/setting";
    static final String COMMAND_REGISTER = "/register";
    static final String COMMAND_SEND = "/send";
    static final String HELP_TEXT = "Этот бот-путеводитель познакомит тебя с историческим городом Боровском.\n\n" +
            "В боте поддерживаются следующие команды: \n\n " +
            "Нажми " + COMMAND_REGISTER + " для регистрации \n\n" +
            "Нажми " + COMMAND_START + " чтобы увидеть приветсвие; \n\n" +
            "Нажми " + COMMAND_MY_DATA + " чтобы увидеть данные, котрые хранит бот о тебе;\n\n" +
            "Нажми " + COMMAND_DELETE_DATA + " чтобы удалить данные о себе;\n\n" +
            "Нажми " + COMMAND_SETTING + " чтобы перейти в настройки.\n\n";

    public TelegramBot(BotConfig config) {
        this.config = config;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand(COMMAND_START, "запустить бота"));
        listOfCommands.add(new BotCommand(COMMAND_MY_DATA, "посмотреть свои данные"));
        listOfCommands.add(new BotCommand(COMMAND_DELETE_DATA, "удалить свои данные"));
        listOfCommands.add(new BotCommand(COMMAND_HELP, "информация как пользоваться этим ботом"));
        listOfCommands.add(new BotCommand(COMMAND_SETTING, "настройки"));
        listOfCommands.add(new BotCommand(COMMAND_REGISTER, "регистрация"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));

        } catch (TelegramApiException e) {
            log.error("Error setting bot's command list: " + e.getMessage());
        }

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

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            if (messageText.contains(COMMAND_SEND) && config.getOwnerID() == chatId) {
                var textToSend = EmojiParser.parseToUnicode(messageText.substring(messageText.indexOf(" ")));
                var users = userRepository.findAll();
                sendingMessage(users,textToSend);
            } else {
                switch (messageText) {
                    case COMMAND_START:
                        registeredUser(update.getMessage());
                        startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                        break;

                    case COMMAND_HELP:
                        prepareAndSendMessage(chatId, HELP_TEXT);
                        break;

                    case COMMAND_REGISTER:
                        register(chatId);
                        break;

                    default:
                        prepareAndSendMessage(chatId, "Извини, это пока не работает.");
                }
            }
        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            if (callbackData.equals(YES_BUTTON)) {
                String text = "Ты зарегистррирован!";
                executeEditMessageText(text, chatId, messageId);

            } else if (callbackData.equals(NO_BUTTON)) {
                String text = "Отмена регистрации...";
                executeEditMessageText(text, chatId, messageId);
            }

        }
    }

    private void registeredUser(Message msg) {

        if (userRepository.findById(msg.getChatId()).isEmpty()) {

            var chatId = msg.getChatId();
            var chat = msg.getChat();

            User user = new User();

            user.setChatId(chatId);
            user.setFirstName(chat.getFirstName());
            user.setLastName(chat.getLastName());
            user.setUserName(chat.getUserName());
            user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));

            userRepository.save(user);
            log.info("Registered user: " + user);
        }
    }

    private void startCommandReceived(long chatId, String name) {
        String answer = EmojiParser.parseToUnicode("Привет, " + name + "!\n"
                + "Рад знакомству!" + " :rocket:");
        log.info("Replied to user " + name);
        sendMessage(chatId, answer);
    }


    private void register(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Ты хочешь зарегистрироваться?");

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        var yesButton = new InlineKeyboardButton();

        yesButton.setText("Да");
        yesButton.setCallbackData(YES_BUTTON);

        var noButton = new InlineKeyboardButton();

        noButton.setText("Нет");
        noButton.setCallbackData(NO_BUTTON);

        rowInLine.add(yesButton);
        rowInLine.add(noButton);

        rowsInLine.add(rowInLine);

        markupInline.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInline);

        executeMessage(message);
    }

    private void executeEditMessageText(String text, long chatId, long messageId) {
        EditMessageText message = new EditMessageText();
        message.setChatId(chatId);
        message.setText(text);
        message.setMessageId((int) messageId);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(ERROR_TEXT + e.getMessage());
        }
    }

    private void executeMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(ERROR_TEXT + e.getMessage());
        }
    }

    private  void prepareAndSendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);
        executeMessage(message);
    }
    private void sendMessage(long chatId, String textToSend) {
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

        executeMessage(message);

    }

    @Scheduled(cron = "${cron.scheduler}")
    private void sendAds() {

        var ads = adsRepository.findAll();
        var users = userRepository.findAll();

        for (Ads ad : ads) {
            sendingMessage(users, ad.getAd());
        }

    }

    private void sendingMessage(Iterable<User> users,String textToSend) {
        for (User user : users) {
            prepareAndSendMessage(user.getChatId(), textToSend);
        }
    }
}

