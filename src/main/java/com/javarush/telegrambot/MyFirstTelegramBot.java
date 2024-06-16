package com.javarush.telegrambot;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.Map;

import static com.javarush.telegrambot.TelegramBotContent.*;

public class MyFirstTelegramBot extends MultiSessionTelegramBot {
    public static final String NAME = "************"; // TODO: добавьте имя бота в кавычках
    public static final String TOKEN = "*********************"; //TODO: добавьте токен бота в кавычках


    public MyFirstTelegramBot() {
        super(NAME, TOKEN);
    }

    @Override
    public void onUpdateEventReceived(Update update) {
        if (getMessageText().equals("/start")) {
            sendPhotoMessageAsync("cat_1_pic");
            sendTextMessageAsync(STEP_1_TEXT,
                    Map.of("ПРОБИРКИ И ПЕРЕЧЕНЬ АНАЛИЗОВ К НИМ", "step_1_btn",
                            "БАК ПОСЕВЫ + COVID", "step_2_btn",
                            "заполнение журнала учета лс на конец месяца", "step_3_btn"));
        }

        if (getCallbackQueryButtonKey().equals("step_1_btn")) {
            sendPhotoMessageAsync("cat_2_pic");
            sendTextMessageAsync(STEP_2_TEXT,
                    Map.of("ПРОБИРКА для КАК", "step_4_btn",
                            "ПРОБИРКА для БИОХИМИИ", "step_5_btn",
                            "ПРОБИРКА для КОАГУЛОГРАММЫ", "step_6_btn",
                            "ПРОБИРКА на ЭТАНОЛ", "step_7_btn",
                            "ПРОБИРКА для ГАЗОВ КРОВИ (КОС)", "step_8_btn"));
        }

        if (getCallbackQueryButtonKey().equals("step_4_btn")) {
            sendPhotoMessageAsync("kak");
            sendTextMessageAsync(STEP_3_TEXT);
        }

        if (getCallbackQueryButtonKey().equals("step_5_btn")) {
            sendPhotoMessageAsync("bh");
            sendTextMessageAsync(STEP_4_TEXT);
        }

        if (getCallbackQueryButtonKey().equals("step_6_btn")) {
            sendPhotoMessageAsync("koa");
            sendTextMessageAsync(STEP_5_TEXT);
        }

        if (getCallbackQueryButtonKey().equals("step_7_btn")) {
            sendPhotoMessageAsync("etan");
            sendTextMessageAsync(STEP_6_TEXT);
        }

        if (getCallbackQueryButtonKey().equals("step_8_btn")) {
            sendPhotoMessageAsync("gaz");
            sendTextMessageAsync(STEP_7_TEXT);
        }

        if (getCallbackQueryButtonKey().equals("step_2_btn")) {
            sendPhotoMessageAsync("prob_1_pic");
            sendTextMessageAsync(STEP_17_TEXT);
            sendPhotoMessageAsync("pk");
            sendTextMessageAsync(STEP_8_TEXT);
            sendPhotoMessageAsync("kal");
            sendTextMessageAsync(STEP_9_TEXT);
            sendPhotoMessageAsync("kal");
            sendTextMessageAsync(STEP_10_TEXT);
            sendPhotoMessageAsync("kal");
            sendTextMessageAsync(STEP_11_TEXT);
            sendPhotoMessageAsync("kal");
            sendTextMessageAsync(STEP_12_TEXT);
            sendPhotoMessageAsync("kal");
            sendTextMessageAsync(STEP_13_TEXT);
            sendPhotoMessageAsync("disgr");
            sendTextMessageAsync(STEP_14_TEXT);
            sendTextMessageAsync(STEP_15_TEXT);
        }

        if (getCallbackQueryButtonKey().equals("step_3_btn")) {
            sendPhotoMessageAsync("cat_3_pic");
            sendTextMessageAsync(STEP_16_TEXT);
        }
    }

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(new MyFirstTelegramBot());
    }
    
}