package com.pizzeria.interfaces;

/**
 * Интерфейс для объектов, которые могут получать уведомления
 */
public interface Notifiable {
    /**
     * Отправить уведомление
     */
    void sendNotification(String message);

    /**
     * Получить контактную информацию для уведомлений
     */
    String getContactInfo();

    /**
     * Проверить, включены ли уведомления
     */
    boolean isNotificationEnabled();
}
