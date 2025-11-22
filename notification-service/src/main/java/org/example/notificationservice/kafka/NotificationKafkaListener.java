package org.example.notificationservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.common.dto.UserNotificationDto;
import org.example.notificationservice.service.NotificationProcessorService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Kafka-листенер для NotificationService.
 * Слушает сообщения из топика user-notifications, переводит
 * в {@link UserNotificationDto} и передаёт в {@link NotificationProcessorService}.
 */
@Service
public class NotificationKafkaListener {

    private final NotificationProcessorService processorService;
    private final ObjectMapper mapper;

    public NotificationKafkaListener(NotificationProcessorService processorService,
                                     ObjectMapper mapper) {
        this.processorService = processorService;
        this.mapper = mapper;
    }

    /**
     * Метод, который вызывается при получении сообщения из Kafka-топика.
     * Сообщение ожидается в формате JSON, соответствующем {@link UserNotificationDto}.
     * В случае ошибки она выводится в консоль.
     *
     * @param message JSON-строка с уведомлением пользователя
     */
    @KafkaListener(topics = "user-notifications", groupId = "notification-group")
    public void listen(String message) {
        try {
            UserNotificationDto dto = mapper.readValue(message, UserNotificationDto.class);
            processorService.process(dto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
