package com.example.telegram.model.enums;

public enum ECommand {
    MENU("/menu"), LOGIN("/login"), REGISTER("/register"), RUN("/run"), CREATE("/create"), SHOW("/show"), RETURN("/return"),
    START("/start"), SIGNOUT("/signout");

    private String command;

    ECommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
