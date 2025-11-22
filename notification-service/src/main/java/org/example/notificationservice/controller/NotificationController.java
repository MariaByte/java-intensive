package org.example.notificationservice.controller;

import org.example.common.dto.UserNotificationDto;
import org.example.notificationservice.service.NotificationProcessorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST-контроллер для отправки уведомлений пользователям.
 * Принимает DTO с информацией о пользователе и типе уведомления
 * и отдаёт обработку {@link NotificationProcessorService}.
 */
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationProcessorService processorService;

    public NotificationController(NotificationProcessorService processorService) {
        this.processorService = processorService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody UserNotificationDto request) {
        processorService.process(request);

        return ResponseEntity.ok("Email отправлен");
    }
}
