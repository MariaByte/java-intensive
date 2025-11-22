package org.example.notificationservice.service;

import org.example.common.dto.UserNotificationDto;
import org.example.notificationservice.email.EmailService;
import org.springframework.stereotype.Service;

/**
 * Сервис, отвечающий за обработку пользовательских уведомлений и отправку email-сообщений пользователям.
 * В зависимости от типа события создаёт соответствующее письмо
 * и отправляет его через {@link EmailService}.
 */
@Service
public class NotificationProcessorServiceImpl implements NotificationProcessorService {

    private final EmailService emailService;

    public NotificationProcessorServiceImpl(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
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
