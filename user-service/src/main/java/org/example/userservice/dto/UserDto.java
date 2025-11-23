package org.example.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

/**
 * DTO для представления пользователя.
 * Используется для передачи данных между слоями приложения (контроллер и сервис).
 */
@Schema(description = "DTO для пользователя, содержащий идентификатор, имя, email, возраст и дату создания")
public class UserDto {

    @Schema(description = "Уникальный идентификатор пользователя", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private int id;

    @NotBlank(message = "Имя пользователя не может быть пустым")
    @Schema(description = "Имя пользователя")
    private String name;

    @NotBlank(message = "Email не может быть пустым")
    @Schema(description = "Email пользователя")
    private String email;

    @Min(value = 0, message = "Возраст не может быть отрицательным")
    @Schema(description = "Возраст пользователя")
    private int age;

    @Schema(description = "Дата и время создания пользователя")
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
