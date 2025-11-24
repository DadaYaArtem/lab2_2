package com.pizzeria.exceptions;

/**
 * Исключение для некорректной аутентификации
 */
public class InvalidAuthenticationException extends Exception {
    private String username;

    public InvalidAuthenticationException(String username) {
        super("Неверное имя пользователя или пароль для: " + username);
        this.username = username;
    }

    public InvalidAuthenticationException(String username, String reason) {
        super("Ошибка аутентификации для " + username + ": " + reason);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
