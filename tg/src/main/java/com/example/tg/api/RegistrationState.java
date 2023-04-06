package com.example.tg.api;

public enum RegistrationState {
    ASK_USERNAME(1),
    ASK_EMAIL(2),
    ASK_PASSWORD(3),
    ASK_NAME(4),
    ASK_LAST_NAME(5),
    REGISTERED(6),
    ALREADY_REGISTERED(7);

    private int state;

    RegistrationState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }
}
