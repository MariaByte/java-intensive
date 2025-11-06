package services;

import dao.UserDao;
import entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit-тесты для {@link UserService} выполнены с использованием Mockito.
 * Проверяется бизнес-логика и корректность работы с {@link UserDao}.
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService;

    /**
     * Проверяет, что при корректных данных сервис вызывает метод create() у DAO.
     */
    @Test
    public void createUserShouldSucceedWhenDataIsValid() {
        userService.createUser("Маша", "masha@mail.ru", 25);
        verify(userDao).create(any(UserEntity.class));
    }

    /**
     * Проверяет, что при пустом имени выбрасывается исключение IllegalArgumentException.
     */
    @Test
    void createUserShouldThrowExceptionWhenNameIsEmpty() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userService.createUser("", "masha@mail.ru", 25)
        );
        assertEquals("Имя пользователя не может быть пустым", exception.getMessage());
    }

    /**
     * Проверяет, что при пустом email выбрасывается исключение IllegalArgumentException.
     */
    @Test
    void createUserShouldThrowExceptionWhenEmailIsEmpty() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userService.createUser("Маша", "", 25)
        );
        assertEquals("Email не может быть пустым", exception.getMessage());
    }

    /**
     * Проверяет, что при отрицательном возрасте выбрасывается исключение IllegalArgumentException.
     */
    @Test
    void createUserShouldThrowExceptionWhenAgeIsNegative() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userService.createUser("Маша", "masha@mail.ru", -25)
        );
        assertEquals("Возраст не может быть отрицательным", exception.getMessage());
    }

    /**
     * Проверяет, что метод getById() возвращает пользователя, если DAO вернёт существующий объект.
     */
    @Test
    void getUserByIdShouldReturnUserWhenExists() {
        UserEntity user = new UserEntity("Коля", "kolya@mail.ru", 30);
        when(userDao.getById(1)).thenReturn(user);

        UserEntity result = userService.getUserById(1);
        assertEquals("Коля", result.getName());
        assertEquals("kolya@mail.ru", result.getEmail());
        verify(userDao).getById(1);
    }

    /**
     * Проверяет, что метод getAll() возвращает список всех пользователей, если DAO их вернёт.
     */
    @Test
    void getAllUsersShouldReturnAllUsers() {
        UserEntity user1 = new UserEntity("Алиса", "alice@mail.ru", 25);
        UserEntity user2 = new UserEntity("Вова", "vova@mail.ru", 28);
        List<UserEntity> users = Arrays.asList(user1, user2);

        when(userDao.getAll()).thenReturn(users);

        List<UserEntity> result = userService.getAllUsers();
        assertEquals(2, result.size());
        verify(userDao).getAll();
    }

    /**
     * Проверяет, что метод getAllUsers возвращает пустой список,
     * если DAO не содержит пользователей.
     */
    @Test
    void getAllUsersShouldReturnEmptyListWhenNoUsersExist() {
        when(userDao.getAll()).thenReturn(Collections.emptyList());
        List<UserEntity> result = userService.getAllUsers();
        assertTrue(result.isEmpty());
        verify(userDao).getAll();
    }

    /**
     * Проверяет, что updateUser() корректно обновляет данные, если пользователь существует.
     */
    @Test
    void updateUserShouldSucceedWhenUserExistsAndDataIsValid() {
        UserEntity existingUser = new UserEntity("Алиса", "alice@mail.ru", 25);
        when(userDao.getById(1)).thenReturn(existingUser);

        userService.updateUser(1, "Алиса Обновлённая", "alice.updated@mail.ru", 26);

        assertEquals("Алиса Обновлённая", existingUser.getName());
        assertEquals("alice.updated@mail.ru", existingUser.getEmail());
        assertEquals(26, existingUser.getAge());
        verify(userDao).update(existingUser);
    }

    /**
     * Проверяет, что при попытке обновления методом updateUser() выбрасывается IllegalArgumentException,
     * если пользователь не существует.
     */
    @Test
    void updateUserShouldThrowExceptionWhenUserDoesNotExist() {
        when(userDao.getById(1)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userService.updateUser(1, "Алиса", "alice@mail.ru", 25)
        );
        assertEquals("Пользователь с ID 1 не найден", exception.getMessage());
    }

    /**
     * Проверяет, что метод deleteUser() вызывает удаление пользователя в DAO.
     */
    @Test
    void deleteUserShouldCallDaoDelete() {
        userService.deleteUser(1);
        verify(userDao).delete(1);
    }
}
