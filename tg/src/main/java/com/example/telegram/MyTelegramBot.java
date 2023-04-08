
package com.example.telegram;

import com.example.telegram.model.dto.request.LoginRequest;
import com.example.telegram.model.dto.request.TaskRequest;
import com.example.telegram.model.dto.response.JwtResponse;
import com.example.telegram.model.enums.BotState;
import com.example.telegram.model.enums.ECommand;
import com.example.telegram.model.enums.LoginState;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Component
public class MyTelegramBot extends TelegramLongPollingBot {

    private LoginState currentState;
    private BotState botState;
    private int STATUS_CODE;

    public MyTelegramBot() {
        botState = BotState.AFK;
        currentState = LoginState.ASK_USERNAME;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "Начать работу с ботом"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException E) {
            E.printStackTrace();
        }
    }

    public int getSTATUS_CODE() {
        return STATUS_CODE;
    }

    public void setSTATUS_CODE(int STATUS_CODE) {
        this.STATUS_CODE = STATUS_CODE;
    }

    LoginRequest loginUser = new LoginRequest();

    @Override
    public String getBotUsername() {
        return "kookacreq_bot";
    }

    @Override
    public String getBotToken() {
        return "6046387088:AAE4IUKNd0kfyrmwSx7Kd_r8XDdUzZy7oag";
    }

    @Override
    public void onUpdateReceived(Update update) {
        long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        if (update.hasMessage() && update.getMessage().hasText()) {
            if (text.equals(ECommand.START.getCommand())) {
                botState = BotState.MENU;
            }
            switch (botState) {
                case MENU -> {
                    sendMessage(chatId, "Войдите в аккаунт с помощью команды "
                            + ECommand.LOGIN.getCommand());
                    botState = BotState.LOGIN;
                }
                case LOGIN -> {
                    switch (currentState) {
                        case ASK_USERNAME -> {
                            if (text.equals(ECommand.LOGIN.getCommand())) {
                                sendMessage(chatId, "Введите имя пользователя:");
                                currentState = LoginState.ASK_PASSWORD;
                            } else {
                                sendMessage(chatId, "Неверная команда!");
                            }
                        }
                        case ASK_PASSWORD -> {
                            loginUser.setUsername(text);
                            sendMessage(chatId, "Введите пароль:");
                            currentState = LoginState.LOGIN_PROCESSING;
                        }
                        case LOGIN_PROCESSING -> {
                            loginUser.setPassword(text);
                            try {
                                sendSignInRequest(loginUser);
                                if (getSTATUS_CODE() == 401) {
                                    sendMessage(chatId, "Неверные имя пользователя или пароль. Повторите попытку!");
                                    botState = BotState.MENU;
                                    currentState = LoginState.ASK_USERNAME;
                                }
                                if (getSTATUS_CODE() == 200) {
                                    sendMessage(chatId, "Вы успешно вошли в аккаунт!");
                                    sendMessage(chatId, "Для дальнейших действий используйте команду " +
                                            ECommand.RUN.getCommand());
                                    botState = BotState.IN_ACCOUNT;
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
                case IN_ACCOUNT -> {
                    if (text.equals(ECommand.RUN.getCommand()) || text.equals(ECommand.RETURN.getCommand())) {
                        sendMessage(chatId, "Приступим к планированию!\n" +
                                "Вы можете создать новый таск с помощью команды " +
                                ECommand.CREATE.getCommand() + "\n" +
                                "Или же посмотреть список всех тасков с помощью команды " +
                                ECommand.SHOW.getCommand());
                        botState = BotState.NEXT;
                    } else {
                        sendMessage(chatId, "Неверная команда!");
                    }
                }
                case NEXT -> {
                    if (text.equals(ECommand.CREATE.getCommand())) {
                        botState = BotState.CREATE;
                        sendMessage(chatId, "Введите название новой таски:");
                    } else if (text.equals(ECommand.SHOW.getCommand())) {
                        botState = BotState.SHOW;
                        try {
                            JwtResponse jwtResponse = (JwtResponse) jwtFromJsonString(sendSignInRequest(loginUser));
                            String tasks = sendShowTasksRequest(jwtResponse);
                            tasksFromJsonString(tasks, chatId);
                            sendMessage(chatId, "Для выхода в главное меню используйте команду " +
                                    ECommand.RETURN.getCommand());
                            botState = BotState.IN_ACCOUNT;
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                case CREATE -> {
                    TaskRequest taskRequest = new TaskRequest();
                    taskRequest.setData(text);
                    try {
                        JwtResponse jwtResponse = (JwtResponse) jwtFromJsonString(sendSignInRequest(loginUser));
                        String token = jwtResponse.getAccessToken();
                        sendCreateTaskRequest(token, taskRequest);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    sendMessage(chatId, "Задание успешно создано!");
                    sendMessage(chatId, "Для выхода в главное меню используйте команду " +
                            ECommand.RETURN.getCommand());
                    botState = BotState.IN_ACCOUNT;
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
        ArrayList<TaskRequest> tasks = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(jsonString);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String data = jsonObject.getString("data");
            TaskRequest taskRequest = new TaskRequest();
            taskRequest.setData(data);
            tasks.add(taskRequest);
        }
        int i = 1;
        StringBuilder stringBuilder = new StringBuilder();
        for (TaskRequest taskRequest : tasks) {
            stringBuilder.append(i + ". " + taskRequest.getData() + "\n");
            i++;
        }
        sendMessage(chatId, stringBuilder.toString());
    }


    private String sendSignInRequest(LoginRequest user) throws IOException {
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
                setSTATUS_CODE(response.getStatusLine().getStatusCode());
                return responseString;
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }

    private void sendCreateTaskRequest(String token, TaskRequest taskRequest) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        Charset charset = Charset.forName("utf-8");
        ContentType contentType = ContentType.create("application/json", charset);
        try {
            HttpPost httpPost = new HttpPost("http://localhost:8080/api/task/create");
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(taskRequest);
            StringEntity entity = new StringEntity(json, contentType);
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            httpPost.setHeader("Authorization", "Bearer " + token);

            CloseableHttpResponse response = httpclient.execute(httpPost);
            try {
                HttpEntity responseEntity = response.getEntity();
                String responseString = EntityUtils.toString(responseEntity, charset);
                EntityUtils.consume(responseEntity);
            } finally {
                response.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            httpclient.close();
        }
    }

    private String sendShowTasksRequest(JwtResponse jwtResponse) throws IOException {
        String token = jwtResponse.getAccessToken();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://localhost:8080/api/task/list");
        httpGet.setHeader("Authorization", "Bearer " + token);
        CloseableHttpResponse response = httpClient.execute(httpGet);
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

