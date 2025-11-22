package org.example.notificationservice;

import com.icegreen.greenmail.util.GreenMail;
import org.example.common.dto.UserNotificationDto;
import org.example.notificationservice.config.TestMailConfig;
import org.example.notificationservice.service.NotificationProcessorService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Интеграционные тесты для проверки отправки уведомлений по email.
 * Тест использует {@link GreenMail} как локальный SMTP-сервер для перехвата писем.
 * Для настройки {@link GreenMail} используется конфигурация {@link TestMailConfig}.
 */
@SpringBootTest(classes = {NotificationServiceApplication.class, TestMailConfig.class})
class EmailIntegrationTest {

    @Autowired
    private GreenMail greenMail;

    @Autowired
    private NotificationProcessorService processorService;

    /**
     * Очищает все письма перед каждым тестом.
     *
     * @throws Exception если возникла ошибка при очистке почтового ящика
     */
    @BeforeEach
    void purgeEmails() throws Exception {
        greenMail.purgeEmailFromAllMailboxes();
    }

    @AfterAll
    static void stopServer(@Autowired GreenMail greenMail) {
        greenMail.stop();
    }

    /**
     * Проверяет отправку уведомления о создании пользователя.
     *
     * @throws Exception если возникла ошибка при получении письма
     */
    @Test
    void testUserCreatedEmail() throws Exception {
        UserNotificationDto dto = new UserNotificationDto("test@example.com", "created");
        processorService.process(dto);

        MimeMessage[] received = greenMail.getReceivedMessages();
        assertEquals(1, received.length);
        assertEquals("Ваш аккаунт создан", received[0].getSubject());
        assertEquals("test@example.com", received[0].getAllRecipients()[0].toString());
    }

    /**
     * Проверяет отправку уведомления об удалении пользователя.
     *
     * @throws Exception если возникла ошибка при получении письма
     */
    @Test
    void testUserDeletedEmail() throws Exception {
        UserNotificationDto dto = new UserNotificationDto("test@example.com", "deleted");
        processorService.process(dto);

        MimeMessage[] received = greenMail.getReceivedMessages();
        assertEquals(1, received.length);
        assertEquals("Ваш аккаунт удалён", received[0].getSubject());
        assertEquals("test@example.com", received[0].getAllRecipients()[0].toString());
    }
}

