package org.example.userservice.controller;

import org.example.userservice.dto.UserDto;
import org.example.userservice.service.UserService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Контроллер для управления пользователями.
 * Предоставляет CRUD операции HTTP-методами:
 * GET /api/users — получение всех пользователей
 * GET /api/users/{id} — получение пользователя по ID
 * POST /api/users — создание нового пользователя
 * PUT /api/users/{id} — обновление пользователя по ID
 * DELETE /api/users/{id} — удаление пользователя по ID
 *
 * Работа с данными ведётся через {@link UserService}.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Получение списка всех пользователей.
     *
     * @return список объектов {@link UserDto}
     */
    @GetMapping
    public List<UserDto> getUsers() {
        return userService.getAllUsers();
    }

    /**
     * Получение пользователя по ID.
     *
     * @param id пользователя
     * @return объект {@link UserDto} для указанного пользователя
     */
    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable int id) {
        return userService.getUserById(id);
    }

    /**
     * Создание нового пользователя.
     *
     * @param user объект {@link UserDto} с данными пользователя
     * @return созданный {@link UserDto}
     */
    @PostMapping
    public UserDto createUser(@RequestBody UserDto user) {
        return userService.createUser(user);
    }

    /**
     * Обновление пользователя по ID.
     *
     * @param id пользователя
     * @param user объект {@link UserDto} с обновлёнными данными
     * @return обновлённый объект {@link UserDto}
     */
    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable int id, @RequestBody UserDto user) {
        return userService.updateUser(id, user);
    }

    /**
     * Удаление пользователя по ID.
     *
     * @param id пользователя для удаления
     */
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
    }
}
