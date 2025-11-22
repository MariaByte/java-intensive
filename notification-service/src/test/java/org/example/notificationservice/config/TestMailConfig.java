package org.example.notificationservice.config;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * Конфигурация почтового сервиса для интеграционных тестов.
 * Настраивает {@link GreenMail} как тестовый SMTP-сервер и {@link JavaMailSenderImpl} для отправки писем
 * на локальный сервер, чтобы тесты могли проверять отправку писем без использования реального SMTP.
 * GreenMail стартует на стандартном тестовом порту SMTP.
 * Аутентификация отключена, TLS не используется.
 */
@Configuration
public class TestMailConfig {

    @Bean
    public GreenMail greenMail() {
        GreenMail greenMail = new GreenMail(ServerSetupTest.SMTP);
        greenMail.start();
        greenMail.setUser("test@example.com", "test", "password");
        return greenMail;
    }

    @Bean
    public JavaMailSenderImpl javaMailSender(GreenMail greenMail) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("localhost");
        mailSender.setPort(ServerSetupTest.SMTP.getPort());
        mailSender.setUsername(null);
        mailSender.setPassword(null);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "false");
        props.put("mail.smtp.starttls.enable", "false");
        props.put("mail.debug", "false");

        return mailSender;
    }
}
