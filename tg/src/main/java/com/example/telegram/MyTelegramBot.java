package com.example.telegram;

import com.example.telegram.model.dto.request.LoginRequest;
import com.example.telegram.model.enums.BotState;
import com.example.telegram.model.enums.ECommand;
import com.example.telegram.service.AuthService;
import com.example.telegram.service.BotService;
import com.example.telegram.service.TaskService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
@Component
public class MyTelegramBot extends TelegramLongPollingBot {

    private BotState botState;
    public final TaskService taskService;
    public final AuthService authService;
    private final BotService botService;
    public LoginRequest loginUser;

    @Value("${tg.bot.token}")
    private String token;

    @Value("${tg.bot.name")
    private String username;


    public MyTelegramBot(TaskService taskService, AuthService authService) {
        this.taskService = taskService;
        this.authService = authService;
        this.loginUser = new LoginRequest();
        botService = new BotService(this);
        botService.initCommands();
        botState = BotState.AFK;
    }

    @Override
    public void onUpdateReceived(Update update) {
        long messageChatId = update.getMessage().getChatId();
        String messageText = update.getMessage().getText();
        if (update.hasMessage() && update.getMessage().hasText()) {
            if (messageText.equals(ECommand.START.getCommand())) {
                botState = BotState.MENU;
            }
            switch (botState) {
                case MENU -> botService.handleMenuState(messageChatId);
                case LOGIN -> botService.handleLoginState(messageChatId, messageText);
                case IN_ACCOUNT -> botService.handleInAccountState(messageChatId, messageText);
                case NEXT -> botService.handleNextState(messageChatId, messageText);
                case CREATE -> botService.handleCreateState(messageChatId, messageText);
            }
            botState = botService.getBotState();
        }
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

}