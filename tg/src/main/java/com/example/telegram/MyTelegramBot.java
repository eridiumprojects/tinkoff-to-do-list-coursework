package com.example.telegram;

import com.example.telegram.model.dto.request.LoginRequest;
import com.example.telegram.model.dto.request.TaskRequest;
import com.example.telegram.model.dto.response.JwtResponse;
import com.example.telegram.model.enums.BotState;
import com.example.telegram.model.enums.ECommand;
import com.example.telegram.model.enums.EMessage;
import com.example.telegram.model.enums.LoginState;
import com.example.telegram.service.AuthService;
import com.example.telegram.service.TaskService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MyTelegramBot extends TelegramLongPollingBot {
    private LoginState currentState;
    private BotState botState;

    public final TaskService taskService;

    public final AuthService authService;

    public MyTelegramBot(TaskService taskService, AuthService authService) {
        this.taskService = taskService;
        this.authService = authService;
        botState = BotState.AFK;
        currentState = LoginState.ASK_USERNAME;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand(ECommand.START.getCommand(), EMessage.INFO_START_MESSAGE.getMessage()));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException E) {
            E.printStackTrace();
        }
    }

    LoginRequest loginUser = new LoginRequest();

    //todo разделить все кейсы на свои методы, все методы должны находиться в классе TelegramService

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
                    sendMessage(chatId, EMessage.LOGIN_IN_ACCOUNT_WITH_MESSAGE.getMessage()
                            + ECommand.LOGIN.getCommand());
                    botState = BotState.LOGIN;
                }
                case LOGIN -> {
                    switch (currentState) {
                        case ASK_USERNAME -> {
                            if (text.equals(ECommand.LOGIN.getCommand())) {
                                sendMessage(chatId, EMessage.INPUT_USERNAME_MESSAGE.getMessage());
                                currentState = LoginState.ASK_PASSWORD;
                            } else {
                                sendMessage(chatId, EMessage.INVALID_COMMAND_MESSAGE.getMessage());
                            }
                        }
                        case ASK_PASSWORD -> {
                            loginUser.setUsername(text);
                            sendMessage(chatId, EMessage.INPUT_PASSWORD_MESSAGE.getMessage());
                            currentState = LoginState.LOGIN_PROCESSING;
                        }
                        case LOGIN_PROCESSING -> {
                            loginUser.setPassword(text);
                            try {
                                authService.sendSignInRequest(loginUser);
                                if (authService.getStatusCode() == 401) {
                                    sendMessage(chatId, EMessage.INVALID_DATA_MESSAGE.getMessage());
                                    botState = BotState.MENU;
                                    currentState = LoginState.ASK_USERNAME;
                                }
                                if (authService.getStatusCode() == 200) {
                                    sendMessage(chatId, EMessage.SUCCESSFULLY_LOGGED_MESSAGE.getMessage());
                                    sendMessage(chatId, EMessage.NEXT_ACTS_MESSAGE.getMessage() +
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
                        sendMessage(chatId, EMessage.IN_ACCOUNT_FIRST_MESSAGE.getMessage() +
                                ECommand.CREATE.getCommand() +
                                EMessage.IN_ACCOUNT_SECOND_MESSAGE.getMessage() +
                                ECommand.SHOW.getCommand());
                        botState = BotState.NEXT;
                    } else {
                        sendMessage(chatId, EMessage.INVALID_COMMAND_MESSAGE.getMessage());
                    }
                }
                case NEXT -> {
                    if (text.equals(ECommand.CREATE.getCommand())) {
                        botState = BotState.CREATE;
                        sendMessage(chatId, EMessage.INPUT_TASK_DATA_MESSAGE.getMessage());
                    } else if (text.equals(ECommand.SHOW.getCommand())) {
                        botState = BotState.SHOW;
                        try {
                            JwtResponse jwtResponse = (JwtResponse) authService.jwtFromJsonString(authService.sendSignInRequest(loginUser));
                            String tasks = taskService.sendShowTasksRequest(jwtResponse);
                            if (tasks.equals("[]")) {
                                sendMessage(chatId, EMessage.EMPTY_LIST_MESSAGE.getMessage()) ;
                            } else {
                                sendMessage(chatId,taskService.tasksFromJsonString(tasks, chatId));
                            }
                            sendMessage(chatId, EMessage.RETURN_MESSAGE.getMessage() +
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
                        JwtResponse jwtResponse = (JwtResponse) authService.jwtFromJsonString(authService.sendSignInRequest(loginUser));
                        String token = jwtResponse.getAccessToken();
//                        sendCreateTaskRequest(token, taskRequest);
                        taskService.sendCreateTaskRequest(token, taskRequest);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    sendMessage(chatId, EMessage.TASK_CREATED_MESSAGE.getMessage());
                    sendMessage(chatId, EMessage.RETURN_MESSAGE.getMessage() +
                            ECommand.RETURN.getCommand());
                    botState = BotState.IN_ACCOUNT;
                }
            }
        }

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

    @Override
    public String getBotUsername() {
        return "kookacreq_bot";
    }

    @Override
    public String getBotToken() {
        return "6046387088:AAF-PH46m_7khQ6_3N9E4QQp2FUjWC9TLYQ";
    }

}