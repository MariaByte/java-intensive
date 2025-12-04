package org.example.userservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.example.common.dto.UserNotificationDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Реализация {@link NotificationProducer}, которая отправляет уведомления через Kafka.
 * Конвертирует {@link UserNotificationDto} в JSON и отправляет в топик "user-notifications".
 */
@Service
public class KafkaNotificationProducer implements NotificationProducer {

    private KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper mapper;

    /**
     * Создаёт KafkaNotificationProducer с указанным KafkaTemplate.
     *
     * @param kafkaTemplate шаблон для отправки сообщений в Kafka
     */
    public KafkaNotificationProducer(KafkaTemplate<String, String> kafkaTemplate,
                                     ObjectMapper mapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.mapper = mapper;
    }

    /**
     * {@inheritDoc}
     *
     * Формирует уведомление типа "created" и отправляет его в Kafka.
     */
    @Override
    @CircuitBreaker(name = "kafkaCB", fallbackMethod = "fallbackSend")
    public void sendUserCreatedNotification(String email) {
        sendNotification(email, "created");
    }

    /**
     * {@inheritDoc}
     *
     * Формирует уведомление типа "deleted" и отправляет его в Kafka.
     */
    @Override
    @CircuitBreaker(name = "kafkaCB", fallbackMethod = "fallbackSend")
    public void sendUserDeletedNotification(String email) {
        sendNotification(email, "deleted");
    }

    /**
     * Вспомогательный метод для сериализации {@link UserNotificationDto} в JSON
     * и отправки сообщения в Kafka топик "user-notifications".
     *
     * @param email адрес электронной почты
     * @param type  тип события created/deleted
     * @throws RuntimeException если произошла ошибка при сериализации или отправке сообщения
     */
    private void sendNotification(String email, String type) {
        try {
            UserNotificationDto dto = new UserNotificationDto(email, type);
            String json = mapper.writeValueAsString(dto);
            kafkaTemplate.send("user-notifications", json).get();
        } catch (Exception e) {
            throw new RuntimeException("Failed to send Kafka message", e);
        }
    }

    /**
     * Вызывается, если отправка сообщения в Kafka не удалась и сработал CircuitBreaker.
     *
     * @param email адрес электронной почты
     * @param cause причина сбоя, вызвавшая активацию CircuitBreaker
     */
    public void fallbackSend(String email, Throwable cause) {
        System.out.println("Kafka fallback: сообщение не отправлено для " + email);
        System.out.println("Причина: " + cause.getMessage());
    }
}
