package org.example.userservice.service;

import org.example.userservice.dto.UserDto;

import java.util.List;

/**
 * Интерфейс для управления пользователями.
 * Определяет CRUD операции, валидацию данных и преобразование между сущностями и DTO.
 */
public interface UserService {

    /**
     * Создание пользователя.
     *
     * @param userDto данные пользователя
     * @return найденный пользователь
     */
    UserDto createUser(UserDto userDto);

    /**
     * Получение пользователя по id.
     *
     * @param id идентификатор пользователя
     * @return найденный пользователь
     */
    UserDto getUserById(int id);

    /**
     * Получение всех пользователей.
     *
     * @return список пользователей
     */
    List<UserDto> getAllUsers();

    /**
     * Обновление пользователя.
     *
     * @param id идентификатор пользователя
     * @param userDto новые данные
     * @return обновлённый пользователь
     */
    UserDto updateUser(int id, UserDto userDto);

    /**
     * Удаление пользователя по id.
     *
     * @param id идентификатор пользователя
     */
    void deleteUser(int id);
}