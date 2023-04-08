package com.example.telegram.model.enums;

public enum LoginState {
    ASK_USERNAME(1),
    ASK_PASSWORD(2),
    LOGIN_PROCESSING(3),
    AFK(4);
    private int state;

    LoginState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }
}
