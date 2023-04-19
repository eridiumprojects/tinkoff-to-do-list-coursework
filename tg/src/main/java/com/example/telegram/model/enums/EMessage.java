package com.example.telegram.model.enums;

public enum EMessage {
    INFO_START_MESSAGE("Начать работу с ботом"),
    LOGIN_IN_ACCOUNT_WITH_MESSAGE("Войдите в аккаунт с помощью команды "),
    INPUT_USERNAME_MESSAGE("Введите имя пользователя:"),
    INPUT_PASSWORD_MESSAGE("Введите пароль:"),
    INVALID_COMMAND_MESSAGE("Неверная команда!"),
    INVALID_DATA_MESSAGE("Неверные имя пользователя или пароль. Повторите попытку!"),
    SUCCESSFULLY_LOGGED_MESSAGE("Вы успешно вошли в аккаунт!"),
    IN_ACCOUNT_FIRST_MESSAGE("Приступим к планированию!\n" +
            "Вы можете создать новый таск с помощью команды "),
    IN_ACCOUNT_SECOND_MESSAGE("\nТакже вы можете посмотреть список всех тасков с помощью команды "),
    IN_ACCOUNT_THIRD_MESSAGE("\nИли выйти из аккаунта с помощью "),
    NEXT_ACTS_MESSAGE("Для дальнейших действий используйте команду "),
    INPUT_TASK_DATA_MESSAGE("Введите название новой таски:"),
    RETURN_MESSAGE("Для выхода в главное меню используйте команду "),
    TASK_CREATED_MESSAGE("Задание успешно создано!"),
    EMPTY_LIST_MESSAGE("Ваш список заданий пуст"),
    SIGNOUT_MESSAGE("Вы успешно вышли из аккаунта!");
    private String message;


    EMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
