package org.example.notificationservice.service;

import org.example.common.dto.UserNotificationDto;
import org.example.notificationservice.email.EmailService;
import org.springframework.stereotype.Service;

/**
 * Сервис для обработки уведомлений пользователей.
 * Получает DTO с информацией о событии пользователя ({@link UserNotificationDto}) и
 * отправляет email через {@link EmailService}.
 */
@Service
public class NotificationProcessorService {

    private final EmailService emailService;

    public NotificationProcessorService(EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * Обрабатывает уведомление пользователя.
     *
     * @param message объект {@link UserNotificationDto}, содержащий email пользователя и тип события
     * @throws IllegalArgumentException если тип события неизвестен
     */
    public void process(UserNotificationDto message) {

        switch (message.getType()) {
            case "created" -> emailService.send(
                    message.getEmail(),
                    "Ваш аккаунт создан",
                    "Здравствуйте! Ваш аккаунт на сайте был успешно создан."
            );
            case "deleted" -> emailService.send(
                    message.getEmail(),
                    "Ваш аккаунт удалён",
                    "Здравствуйте! Ваш аккаунт был удалён."
            );
            default -> throw new IllegalArgumentException("Неизвестный тип события: "  + message.getType());
        }
    }

}
