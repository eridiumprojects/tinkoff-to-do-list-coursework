package com.example.telegram.service;

import com.example.telegram.model.dto.request.LoginRequest;
import com.example.telegram.model.dto.request.TaskRequest;
import com.example.telegram.model.dto.response.JwtResponse;
import com.example.telegram.model.enums.BotState;
import com.example.telegram.model.enums.ECommand;
import com.example.telegram.model.enums.EMessage;
import com.example.telegram.model.enums.LoginState;
import com.example.telegram.util.RequestBuilder;
import lombok.*;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Getter
@Setter
public class BotService {
    private BotState botState;
    private LoginState currentState;
    private AuthService authService;
    private TaskService taskService;
    private TelegramLongPollingBot bot;
    public LoginRequest loginUser;

    Map<Long, BotState> map = new HashMap<>();
    Jedis jedis = new Jedis("localhost",6379);


    public BotService(TelegramLongPollingBot bot) {
        this.bot = bot;
        currentState = LoginState.ASK_USERNAME;
        this.loginUser = new LoginRequest();
        this.authService = new AuthService(new RequestBuilder());
        this.taskService = new TaskService(new RequestBuilder());
        jedis.connect();
    }

    public void handleMenuState(long messageChatId) {
        sendMessage(messageChatId
                , EMessage.LOGIN_IN_ACCOUNT_WITH_MESSAGE.getMessage()
                        + ECommand.LOGIN.getCommand());
        botState = BotState.LOGIN;
    }

    public void handleLoginState(long messageChatId, String messageText) {
        switch (currentState) {
            case ASK_USERNAME -> processAskUsername(messageChatId, messageText);
            case ASK_PASSWORD -> processAskPassword(messageChatId, messageText);
            case LOGIN_PROCESSING -> processLoginProcessing(messageChatId, messageText);
        }
    }

    public void processAskUsername(long messageChatId, String messageText) {
        if (map.containsKey(messageChatId) && map.containsValue(BotState.IN_ACCOUNT)) {
            sendMessage(messageChatId, "Вы уже авторизованы в аккаунт");
            botState = BotState.IN_ACCOUNT;
        } else {
            if (messageText.equals(ECommand.LOGIN.getCommand())) {
                sendMessage(messageChatId, EMessage.INPUT_USERNAME_MESSAGE.getMessage());
                currentState = LoginState.ASK_PASSWORD;
            } else {
                sendMessage(messageChatId, EMessage.INVALID_COMMAND_MESSAGE.getMessage());
            }
        }
    }

    public void processAskPassword(long messageChatId, String messageText) {
        loginUser.setUsername(messageText);
        sendMessage(messageChatId, EMessage.INPUT_PASSWORD_MESSAGE.getMessage());
        currentState = LoginState.LOGIN_PROCESSING;
    }

    public void processLoginProcessing(long messageChatId, String messageText) {
        loginUser.setPassword(messageText);
        try {
            authService.sendSignInRequest(loginUser);
            if (authService.getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
                sendMessage(messageChatId, EMessage.INVALID_DATA_MESSAGE.getMessage());
                botState = BotState.MENU;
                currentState = LoginState.ASK_USERNAME;
            }
            if (authService.getStatusCode() == HttpStatus.SC_OK) {
                map.put(messageChatId, BotState.IN_ACCOUNT);
                jedis.set("chatId",String.valueOf(messageChatId));
                jedis.set("userState", BotState.IN_ACCOUNT.toString());
                sendMessage(messageChatId, EMessage.SUCCESSFULLY_LOGGED_MESSAGE.getMessage());
                sendMessage(messageChatId, EMessage.NEXT_ACTS_MESSAGE.getMessage() +
                        ECommand.RUN.getCommand());
                botState = BotState.IN_ACCOUNT;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleInAccountState(long messageChatId, String messageText) {
        if (messageText.equals(ECommand.RUN.getCommand())
                || messageText.equals(ECommand.RETURN.getCommand())) {
            sendMessage(messageChatId, EMessage.IN_ACCOUNT_FIRST_MESSAGE.getMessage() +
                    ECommand.CREATE.getCommand() +
                    EMessage.IN_ACCOUNT_SECOND_MESSAGE.getMessage() +
                    ECommand.SHOW.getCommand() +
                    EMessage.IN_ACCOUNT_THIRD_MESSAGE.getMessage() +
                    ECommand.SIGNOUT.getCommand());
            botState = BotState.NEXT;
        } else {
            sendMessage(messageChatId, EMessage.INVALID_COMMAND_MESSAGE.getMessage());
        }
    }

    public void handleNextState(long messageChatId, String messageText) {
        if (messageText.equals(ECommand.CREATE.getCommand())) {
            botState = BotState.CREATE;
            sendMessage(messageChatId, EMessage.INPUT_TASK_DATA_MESSAGE.getMessage());
        } else if (messageText.equals(ECommand.SHOW.getCommand())) {
            botState = BotState.SHOW;
            try {
                JwtResponse jwtResponse = (JwtResponse) authService.jwtFromJsonString(authService.sendSignInRequest(loginUser));
                String tasks = taskService.sendShowTasksRequest(jwtResponse);
                if (tasks.equals("[]")) {
                    sendMessage(messageChatId, EMessage.EMPTY_LIST_MESSAGE.getMessage());
                } else {
                    sendMessage(messageChatId, taskService.tasksFromJsonString(tasks));
                }
                sendMessage(messageChatId, EMessage.RETURN_MESSAGE.getMessage() + ECommand.RETURN.getCommand());
                botState = BotState.IN_ACCOUNT;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (messageText.equals(ECommand.SIGNOUT.getCommand())) {
            sendMessage(messageChatId, EMessage.SIGNOUT_MESSAGE.getMessage());
            map.remove(messageChatId);
            botState = BotState.MENU;
        }
    }

    public void handleCreateState(long messageChatId, String messageText) {
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setData(messageText);
        try {
            JwtResponse jwtResponse = (JwtResponse) authService.jwtFromJsonString(authService.sendSignInRequest(loginUser));
            taskService.sendCreateTaskRequest(jwtResponse.getAccessToken(), taskRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        sendMessage(messageChatId, EMessage.TASK_CREATED_MESSAGE.getMessage());
        sendMessage(messageChatId, EMessage.RETURN_MESSAGE.getMessage() + ECommand.RETURN.getCommand());
        botState = BotState.IN_ACCOUNT;
    }

    public void initCommands() {
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand(ECommand.START.getCommand(),
                EMessage.INFO_START_MESSAGE.getMessage()));
        try {
            bot.execute(new SetMyCommands(listOfCommands,
                    new BotCommandScopeDefault(), null));
        } catch (TelegramApiException E) {
            E.printStackTrace();
        }
    }

    public void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
