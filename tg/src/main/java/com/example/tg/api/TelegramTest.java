package com.example.tg.api;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
public class TelegramTest extends TelegramLongPollingBot {
    private RegistrationState currentState;

    public TelegramTest() {
        currentState = RegistrationState.ASK_USERNAME;
    }
    private Map<Long, String> userAnswers = new HashMap<>();
    private ArrayList<String> answers = new ArrayList<>();

    RegisterDTO user = new RegisterDTO();
        @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText()) {
                String text = message.getText();
                switch (currentState) {
                    case ASK_USERNAME:
                        // Обрабатываем сообщение в начальном состоянии
                        if (text.equals("/register")) {
                            // Переходим в следующее состояние
                            currentState = RegistrationState.ASK_EMAIL;
                            // Отправляем ответ пользователю
                            sendResponse(message.getChatId(), "Введите имя пользователя");
                        } else {
                            // Обрабатываем некорректный ввод
                            sendResponse(message.getChatId(), "Некорректный ввод, попробуйте снова");
                        }
                        break;
                    case ASK_EMAIL:
                        user.setUsername(text);
                        // Обрабатываем сообщение в следующем состоянии
                        if (user.getUsername() != null) {
                            // Выполняем действие
                            sendResponse(message.getChatId(), "Введите электронную почту");
                            // Переходим в следующее состояние
                            currentState = RegistrationState.ASK_PASSWORD;
                        } else {
                            // Обрабатываем некорректный ввод
                            sendResponse(message.getChatId(), "Некорректный ввод, попробуйте снова");
                        }
                        break;
                    case ASK_PASSWORD:
                        user.setEmail(text);
                        if (user.getEmail() != null) {
                            sendResponse(message.getChatId(),"Введите пароль");
                            currentState = RegistrationState.ASK_NAME;
                        } else {
                            sendResponse(message.getChatId(), "Некорректный ввод, попробуйте снова");
                        }
                        break;
                    case ASK_NAME:
                        user.setPassword(text);
                        if (user.getPassword() != null) {
                            sendResponse(message.getChatId(),"Введите имя");
                            currentState = RegistrationState.ASK_LAST_NAME;
                        }
                        else {
                            sendResponse(message.getChatId(), "Некорректный ввод, попробуйте снова");
                        }
                        break;
                    case ASK_LAST_NAME:
                        user.setFirstName(text);
                        if (user.getFirstName() != null) {
                            sendResponse(message.getChatId(),"Введите фамилию");
                            currentState = RegistrationState.REGISTERED;
                        }
                        else {
                            sendResponse(message.getChatId(), "Некорректный ввод, попробуйте снова");
                        }
                        break;
                    case REGISTERED:
                        user.setLastName(text);
                        if (user.getLastName() != null) {
                            sendResponse(message.getChatId(),"Пользователь успешно зарегестрирован!");
                        } else {
                            sendResponse(message.getChatId(),"Некорректный ввод, попробуйте снова");
                        }
                        // Обрабатываем сообщение в конечном состоянии
                        String person = user.toString();
                        sendResponse(message.getChatId(), person);
                        break;
                }
            }
        }
    }


//    @Override
//    public void onUpdateReceived(Update update) {
//        if (update.hasMessage() && update.getMessage().hasText()) {
//            Message message = update.getMessage();
//            String text = message.getText();
//            Long chatId = message.getChatId();
//
//            if (message.isReply() && userAnswers.containsKey(chatId)) {
//                // Получаем ответ пользователя на сообщение с принудительным ответом
//                String answer = message.getText();
//                userAnswers.put(chatId, answer);
//                answers.add(answer);
//
//                // Отправляем новое сообщение с использованием клавиатуры
//                SendMessage reply = new SendMessage();
//                reply.setChatId(chatId);
//                reply.setText("Спасибо, ваш ответ принят! Введите следующее сообщение:");
//                reply.setReplyMarkup(getReplyKeyboardMarkup());
////                        .setChatId(chatId)
////                        .setText("Спасибо, ваш ответ принят! Введите следующее сообщение:")
////                        .setReplyMarkup(getReplyKeyboardMarkup());
//                try {
//                    execute(reply);
//                } catch (TelegramApiException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                // Сохраняем сообщение пользователя, если оно не является ответом на сообщение с принудительным ответом
//                userAnswers.put(chatId, text);
//                answers.add(text);
//
//                // Отправляем новое сообщение с использованием клавиатуры
//                SendMessage reply = new SendMessage();
//                reply.setChatId(chatId);
//                reply.setText("Введите следующее сообщение:");
//                reply.setReplyMarkup(getReplyKeyboardMarkup());
////                        .setChatId(chatId)
////                        .setText("Введите следующее сообщение:")
////                        .setReplyMarkup(getReplyKeyboardMarkup());
//                try {
//                    execute(reply);
//                } catch (TelegramApiException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    // Метод для создания клавиатуры с принудительным ответом
//    private ReplyKeyboardMarkup getReplyKeyboardMarkup() {
//        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
//        keyboardMarkup.setResizeKeyboard(true);
//        keyboardMarkup.setOneTimeKeyboard(true);
//
//        KeyboardButton button = new KeyboardButton();
//        button.setText("Ответить на сообщение");
//        button.setRequestContact(false);
//        button.setRequestLocation(false);
//
//        KeyboardRow row = new KeyboardRow();
//        row.add(button);
//        List<KeyboardRow> rows = new ArrayList<>();
//        rows.add(row);
//
//        keyboardMarkup.setKeyboard(rows);
//        return keyboardMarkup;
//    }

    private void sendResponse(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "foxytodo_bot";
    }

    @Override
    public String getBotToken() {
        return "5734029445:AAHw0A0i2yiJ1ujT7vN45a8mFusgbrm7tT0";
    }
}

