package org.example.userservice.service;

/**
 * Интерфейс для отправки уведомлений о событиях пользователя.
 * Определяет методы для уведомления о создании и удалении пользователя.
 */
public interface NotificationProducer {

    /**
     * Отправляет уведомление о создании пользователя на указанный email.
     *
     * @param email адрес электронной почты
     */
    void sendUserCreatedNotification(String email);

    /**
     * Отправляет уведомление об удалении пользователя на указанный email.
     *
     * @param email адрес электронной почты
     */
    void sendUserDeletedNotification(String email);
}
