package org.example.userservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    public void sendUserCreatedNotification(String email) {
        sendNotification(email, "created");
    }

    /**
     * {@inheritDoc}
     *
     * Формирует уведомление типа "deleted" и отправляет его в Kafka.
     */
    @Override
    public void sendUserDeletedNotification(String email) {
        sendNotification(email, "deleted");
    }

    /**
     * Вспомогательный метод для сериализации {@link UserNotificationDto} в JSON
     * и отправки сообщения в Kafka топик "user-notifications".
     *
     * @param email адрес электронной почты
     * @param type  тип события created/deleted
     */
    private void sendNotification(String email, String type) {
        try {
            UserNotificationDto dto = new UserNotificationDto(email, type);
            String json = mapper.writeValueAsString(dto);
            kafkaTemplate.send("user-notifications", json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
