package org.example.notificationservice.service;

import org.example.common.dto.UserNotificationDto;

/**
 * Интерфейс для обработки уведомлений пользователей.
 * Получает DTO с информацией о событии пользователя {@link UserNotificationDto}.
 */
public interface NotificationProcessorService {
    /**
     * Обрабатывает уведомление пользователя.
     *
     * @param message объект {@link UserNotificationDto}, содержащий email пользователя и тип события
     * @throws IllegalArgumentException если тип события неизвестен
     */
    void process(UserNotificationDto message);

}
