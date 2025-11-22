package org.example.notificationservice.email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Реализация {@link EmailService} для отправки email сообщений через {@link JavaMailSender}.
 *
 * Создаёт текстовое письмо и отправляет его на указанный адрес.
 */
@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    /**
     * Создаёт EmailServiceImpl с указанным {@link JavaMailSender}.
     *
     * @param mailSender компонент Spring для отправки писем
     */
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * {@inheritDoc}
     *
     * Формирует {@link SimpleMailMessage} с указанными адресом получателя, темой и телом письма,
     * и отправляет его с помощью {@link JavaMailSender}.
     */
    @Override
    public void send(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("mashulya.rudik008@mail.ru");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}
