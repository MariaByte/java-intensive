package org.example.notificationservice.email;

/**
 * Интерфейс для отправки email.
 * Реализации этого интерфейса отвечают за формирование и доставку
 * писем пользователям по указанным адресам.
 */
public interface EmailService {

    /**
     * Отправляет email.
     *
     * @param to адрес получателя
     * @param subject тема письма
     * @param body тело письма
     */
    void send(String to, String subject, String body);
}
