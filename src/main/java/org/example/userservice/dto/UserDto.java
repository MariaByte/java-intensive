package org.example.userservice.dto;

import java.time.LocalDateTime;

/**
 * DTO для представления пользователя.
 * Используется для передачи данных между слоями приложения (контроллер и сервис).
 * Содержит поля пользователя: идентификатор, имя, email, возраст и дату создания.
 */
public class UserDto {
    private int id;
    private String name;
    private String email;
    private int age;
    private LocalDateTime createdAt;

    public UserDto() {

    }

    public UserDto(int id, String name, String email, int age, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
