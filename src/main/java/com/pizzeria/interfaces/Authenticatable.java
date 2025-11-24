package com.pizzeria.interfaces;

import com.pizzeria.exceptions.InvalidAuthenticationException;

/**
 * Интерфейс для аутентификации
 */
public interface Authenticatable {
    /**
     * Аутентифицировать пользователя
     */
    boolean authenticate(String username, String password) throws InvalidAuthenticationException;

    /**
     * Проверить пароль
     */
    boolean verifyPassword(String password);

    /**
     * Изменить пароль
     */
    void changePassword(String oldPassword, String newPassword) throws InvalidAuthenticationException;
}
