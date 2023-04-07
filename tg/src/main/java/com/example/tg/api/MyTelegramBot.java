
package com.example.tg.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
        listOfCommands.add(new BotCommand(Command.LOGIN.getCommand(), "Войти в существующий аккаунт"));
//        listOfCommands.add(new BotCommand("/create", "Главное меню"));
//        listOfCommands.add(new BotCommand("/register", "Создать аккаунт"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException E) {
            E.printStackTrace();
        }
    }

    LoginDTO loginUser = new LoginDTO();

    @Override
    public String getBotUsername() {
        return "foxytodo_bot";
    }

    @Override
    public String getBotToken() {
        return "5734029445:AAEcg9bDrldnzofqR1xWop1QR3R9YIzse2I";
    }

    @Override
    public void onUpdateReceived(Update update) {
        long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        if (update.hasMessage() && update.getMessage().hasText()) {
            switch (botState) {
                case MENU -> {
                    sendMessage(chatId, "Войдите в аккаунт с помощью команды "
                            + Command.LOGIN.getCommand());
                    botState = BotState.LOGIN;
                }
                case LOGIN -> {
                    switch (currentState) {
                        case ASK_USERNAME -> {
                            sendMessage(chatId, "Введите имя пользователя:");
                            currentState = RegistrationState.ASK_PASSWORD;
                        }
                        case ASK_PASSWORD -> {
                            loginUser.setUsername(text);
                            sendMessage(chatId, "Введите пароль:");
                            currentState = RegistrationState.LOGIN_PROCESSING;
                        }
                        case LOGIN_PROCESSING -> {
                            loginUser.setPassword(text);
                            try {
                                String answer = sendSignInRequest(loginUser, chatId);
                                if (answer.equals("User doesn't exist")) {
                                    sendMessage(chatId, "Такого пользователя не существует. Повторите попытку!");
                                    currentState = RegistrationState.ASK_USERNAME;
                                }
                                if (answer.equals("Error: Invalid password")) {
                                    sendMessage(chatId, "Неверные имя пользователя или пароль. Повторите попытку!");
                                    currentState = RegistrationState.ASK_USERNAME;
                                } else {
                                    sendMessage(chatId, "Вы успешно вошли в аккаунт!");
                                    sendMessage(chatId, "Для дальнейших действий используйте команду "
                                            + Command.START.getCommand());
                                    botState = BotState.IN_ACCOUNT;
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
                case IN_ACCOUNT -> {
                    sendMessage(chatId, "Приступим к планированию!\n" +
                            "Вы можете создать новый таск с помощью команды /create\n" +
                            "Или же посмотреть список всех тасков с помощью команды /show");
                    botState = BotState.NEXT;
                }
                case NEXT -> {
                    if (text.equals(Command.CREATE.getCommand())) {
                        botState = BotState.CREATE;
                    } else if (text.equals(Command.SHOW.getCommand())) {
                        botState = BotState.SHOW;
                        try {
                            JwtResponse jwtResponse = (JwtResponse) jwtFromJsonString(sendSignInRequest(loginUser, chatId));
                            String tasks = sendShowTasksRequest(jwtResponse, chatId);
                            tasksFromJsonString(tasks, chatId);
                            sendMessage(chatId, "Для выхода в главное меню используйте команду " +
                                    Command.RETURN.getCommand());
                            botState = BotState.IN_ACCOUNT;

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        //...
                    }
                }
            }
        }

    }

    public static JwtResponse jwtFromJsonString(String jsonString) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JwtResponse response = mapper.readValue(jsonString, JwtResponse.class);
        return response;
    }

    public void tasksFromJsonString(String jsonString, long chatId) {
        if (jsonString.equals("[]")) {
            sendMessage(chatId, "Ваш список заданий пуст");
            return;
        }
        ArrayList<TaskDTO> tasks = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(jsonString);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String data = jsonObject.getString("data");
            TaskDTO taskDTO = new TaskDTO();
            taskDTO.setData(data);
            tasks.add(taskDTO);
        }
        int i = 1;
        StringBuilder stringBuilder = new StringBuilder();
        for (TaskDTO taskDTO : tasks) {
            stringBuilder.append(i + ". " + taskDTO.getData() + "\n");
            i++;
        }
        sendMessage(chatId, stringBuilder.toString());
    }


    private String sendSignInRequest(LoginDTO user, long chatId) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost("http://localhost:8080/api/auth/signin");
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(user);

            StringEntity entity = new StringEntity(json);
            entity.setContentType("application/json");
            httpPost.setEntity(entity);

            CloseableHttpResponse response = httpclient.execute(httpPost);
            try {
                HttpEntity responseEntity = response.getEntity();
                String responseString = EntityUtils.toString(responseEntity);
                EntityUtils.consume(responseEntity);
                return responseString;
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }

    private String sendShowTasksRequest(JwtResponse jwtResponse, long chatId) throws IOException {
        String token = jwtResponse.getAccessToken();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://localhost:8080/api/task/list");
        httpGet.setHeader("Authorization", "Bearer " + token);
        CloseableHttpResponse response = httpClient.execute(httpGet);
//        System.out.println("Response code: " + response.getStatusLine().getStatusCode());
        BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String inputLine;
        StringBuilder responseBody = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            responseBody.append(inputLine);
        }
        in.close();
        return responseBody.toString();
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

