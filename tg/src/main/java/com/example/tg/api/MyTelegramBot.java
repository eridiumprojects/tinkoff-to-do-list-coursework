
package com.example.tg.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MyTelegramBot extends TelegramLongPollingBot {

    private RegistrationState currentState;
    private BotState botState;

    public MyTelegramBot() {
        currentState = RegistrationState.ASK_USERNAME;
        botState = BotState.MENU;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/menu", "Главное меню"));
        listOfCommands.add(new BotCommand("/register", "Создать аккаунт"));
        listOfCommands.add(new BotCommand("/login", "Войти в существующий аккаунт"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException E) {
            E.printStackTrace();
        }
    }

    RegisterDTO user = new RegisterDTO();

    @Override
    public String getBotUsername() {
        return "foxytodo_bot";
    }

    @Override
    public String getBotToken() {
        return "5734029445:AAHw0A0i2yiJ1ujT7vN45a8mFusgbrm7tT0";
    }

    @Override
    public void onUpdateReceived(Update update) {
        long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        if (update.hasMessage() && update.getMessage().hasText()) {
            switch (botState) {
                case MENU -> {
                    sendMessage(chatId, "Выбери необходимую команду:\n" +
                            "/register\n" +
                            "/login\n");
                    botState = BotState.NEXT;
                }
                case NEXT -> {
                    if (text.equals("/register")) {
                        botState = BotState.REGISTRATION;
                        sendMessage(chatId, "Введите имя пользователя:");
                        currentState = RegistrationState.ASK_EMAIL;

                    } else if (text.equals("/login")) {
                        botState = BotState.LOGIN;
                    } else {
                        sendMessage(chatId, "Некорректный ввод, повторите попытку...");
                    }
                }
                case REGISTRATION -> {
                    switch (currentState) {
                        case ASK_EMAIL -> {
                            user.setUsername(text);
                            sendMessage(chatId, "Введите электронную почту:");
                            currentState = RegistrationState.ASK_PASSWORD;
                        }
                        case ASK_PASSWORD -> {
                            user.setEmail(text);
                            sendMessage(chatId, "Введите пароль:");
                            currentState = RegistrationState.ASK_NAME;
                        }
                        case ASK_NAME -> {
                            user.setPassword(text);
                            sendMessage(chatId, "Введите ваше имя:");
                            currentState = RegistrationState.ASK_LAST_NAME;
                        }
                        case ASK_LAST_NAME -> {
                            user.setFirstName(text);
                            sendMessage(chatId, "Введите ваше фамилия:");
                            currentState = RegistrationState.REGISTERED;
                        }
                        case REGISTERED -> {
                            user.setLastName(text);
//                            user.setStateId(RegistrationState.REGISTERED.getState());
//                            user.setChatId(chatId);
                            try {
                                sendSignupRequest(user);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            sendMessage(chatId, "Пользователь успешно зарегистрирован!");
                            sendMessage(chatId, user.toString());
                            currentState = RegistrationState.ALREADY_REGISTERED;
                        }
                        case ALREADY_REGISTERED -> {
                            sendMessage(chatId, "Вы уже зарегистрированы!");
                        }
                    }
                }
            }
        }
//                case "/login" -> {
//                    //login case
//                }
    }

    private String sendSignupRequest(RegisterDTO user) throws IOException {
        // Создаем объект HttpClient
        CloseableHttpClient httpclient = HttpClients.createDefault();

        try {
            // Создаем объект HttpPost с URL-адресом другого бэкенд-сервиса
            HttpPost httpPost = new HttpPost("http://localhost:8080/api/auth/signup");

            // Создаем объект, содержащий данные, которые мы хотим отправить

            // Преобразуем объект MyData в JSON-строку
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(user);

            // Устанавливаем заголовок Content-Type и добавляем JSON-данные в тело запроса
            StringEntity entity = new StringEntity(json);
            entity.setContentType("application/json");
            httpPost.setEntity(entity);

            // Отправляем запрос и получаем ответ
            CloseableHttpResponse response = httpclient.execute(httpPost);
            try {
                // Обрабатываем ответ
                HttpEntity responseEntity = response.getEntity();
                String responseString = EntityUtils.toString(responseEntity);
                EntityUtils.consume(responseEntity);
                // Делаем что-то с объектом MyResponse
                // ...
            } finally {
                response.close();
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            httpclient.close();
        }
        return user.toString();
    }


    public void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);
        try {
            execute(message);
        } catch (TelegramApiException E) {
            E.getMessage();
        }
    }

}

