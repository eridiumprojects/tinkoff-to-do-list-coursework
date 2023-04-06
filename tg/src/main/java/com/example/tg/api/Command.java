package com.example.tg.api;

public enum Command {
    MENU("/menu"), LOGIN("/login"), REGISTER("/register"), START("/start");

    private String command;

    Command(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}