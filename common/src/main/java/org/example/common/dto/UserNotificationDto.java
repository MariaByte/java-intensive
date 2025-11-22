package org.example.common.dto;

/**
 * DTO для передачи информации о пользовательском уведомлении.
 * Содержит email пользователя и тип уведомления: created/deleted.
 */
public class UserNotificationDto {

    private String email;
    private String type;

    public UserNotificationDto() {}

    public UserNotificationDto(String email, String type) {
        this.email = email;
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
