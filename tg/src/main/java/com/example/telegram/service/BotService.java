package com.example.telegram.service;

import com.example.telegram.model.enums.BotState;
import com.example.telegram.model.enums.ECommand;
import com.example.telegram.model.enums.EMessage;
import lombok.*;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@Getter
@Setter

public class BotService {
    private final AuthService authService;
    private final TaskService taskService;

    public BotService(AuthService authService, TaskService taskService) {
        this.authService = authService;
        this.taskService = taskService;
    }

//    public void handleMenu(long messageChatId) {
//        sendMessage(messageChatId,
//                EMessage.LOGIN_IN_ACCOUNT_WITH_MESSAGE.getMessage()
//                        + ECommand.LOGIN.getCommand());
//    }
}
