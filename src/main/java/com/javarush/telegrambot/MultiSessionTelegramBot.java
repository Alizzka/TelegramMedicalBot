package com.javarush.telegrambot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiSessionTelegramBot extends TelegramLongPollingBot {
    private String name;
    private String token;

    private ThreadLocal<Update> updateEvent = new ThreadLocal<>();
    private HashMap<Long, Integer> gloryStorage = new HashMap<>();

    public MultiSessionTelegramBot(String name, String token) {
        this.name = name;
        this.token = token;
    }

    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public final void onUpdateReceived(Update updateEvent) {
        this.updateEvent.set(updateEvent);
        onUpdateEventReceived(this.updateEvent.get());
    }

    public void onUpdateEventReceived(Update updateEvent) {
        //do nothing
    }

    public Long getCurrentChatId() {
        if (updateEvent.get().hasMessage()) {
            return updateEvent.get().getMessage().getFrom().getId();
        }

        if (updateEvent.get().hasCallbackQuery()) {
            return updateEvent.get().getCallbackQuery().getFrom().getId();
        }

        return null;
    }

    public String getMessageText() {
        return updateEvent.get().hasMessage() ? updateEvent.get().getMessage().getText() : "";
    }

    public String getCallbackQueryButtonKey() {
        return updateEvent.get().hasCallbackQuery() ? updateEvent.get().getCallbackQuery().getData() : "";
    }

    public void sendTextMessageAsync(String text) {
        try {
            SendMessage message = createMessage(text);
            var task = sendApiMethodAsync(message);
            this.sendMessages.add(task.get());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void sendTextMessageAsync(String text, Map<String, String> buttons) {
        try {
            SendMessage message = createMessage(text, buttons);
            var task = sendApiMethodAsync(message);
            this.sendMessages.add(task.get());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void sendPhotoMessageAsync(String photoKey) {
        SendPhoto photo = createPhotoMessage(photoKey);
        executeAsync(photo);
    }


    public SendMessage createMessage( String text) {
        SendMessage message = new SendMessage();
        message.setText(new String(text.getBytes(), StandardCharsets.UTF_8));
        message.setParseMode("markdown");
        Long chatId = getCurrentChatId();
        message.setChatId(chatId);
        return message;
    }

    public SendMessage createMessage( String text, Map<String, String> buttons) {
        SendMessage message = createMessage(text);
        if (buttons != null && !buttons.isEmpty())
            attachButtons(message, buttons);
        return message;
    }

    private void attachButtons(SendMessage message, Map<String, String> buttons) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        for (String buttonName : buttons.keySet()) {
            String buttonValue = buttons.get(buttonName);

            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(new String(buttonName.getBytes(), StandardCharsets.UTF_8));
            button.setCallbackData(buttonValue);

            keyboard.add(List.of(button));
        }

        markup.setKeyboard(keyboard);
        message.setReplyMarkup(markup);
    }

    public SendPhoto createPhotoMessage(String name) {
        try {
            SendPhoto photo = new SendPhoto();
            InputFile inputFile = new InputFile();
            var is = ClassLoader.getSystemResourceAsStream("images/" + name + ".jpg");
            inputFile.setMedia(is, name);

            photo.setPhoto(inputFile);
            Long chatId = getCurrentChatId();
            photo.setChatId(chatId);
            return photo;
        } catch (Exception e) {
            throw new RuntimeException("Can't create photo message!");
        }
    }

    private List<Message> sendMessages = new ArrayList<>();

    public Message getLastSentMessage() {
        if (this.sendMessages.isEmpty()) return null;
        return this.sendMessages.get(this.sendMessages.size() - 1);
    }

    public List<Message> getAllSentMessages() {
        return this.sendMessages;
    }

    public void editTextMessageAsync(Integer messageId, String text) {
        try {
            EditMessageText command = new EditMessageText();
            command.setChatId(getCurrentChatId());
            command.setMessageId(messageId);
            command.setText(text);
            executeAsync(command);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setUserGlory(int glories) {
        gloryStorage.put( getCurrentChatId(), glories);
    }
    public int getUserGlory() {
        return gloryStorage.getOrDefault( getCurrentChatId(), 0);
    }

    public void addUserGlory(int glories) {
        gloryStorage.put(getCurrentChatId(), getUserGlory() + glories);
    }
}
