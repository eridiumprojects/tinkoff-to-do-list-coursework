package com.example.tg.api;

public enum RegistrationState {
    ASK_USERNAME(1),
    ASK_PASSWORD(2),
    LOGIN_PROCESSING(3);
    private int state;

    RegistrationState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }
}
