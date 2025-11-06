package dao;

import entity.UserEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import utils.HibernateUtil;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Интеграционные тесты для {@link UserDaoImpl} с использованием Testcontainers и PostgreSQL.
 * Проверяются реальные CRUD операции через Hibernate.
 */
@Testcontainers
class UserDaoImplTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpassword");

    private static UserDao userDao;

    /**
     * Инициализация контейнера и Hibernate перед выполнением всех тестов.
     * Переопределение свойств Hibernate для использования Testcontainers.
     */
    @BeforeAll
    static void setUpContainer() {
        postgres.start();

        HibernateUtil.overrideHibernateProperties(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        );

        userDao = new UserDaoImpl();
    }

    /**
     * Завершение работы Hibernate после всех тестов.
     * Закрывает SessionFactory.
     */
    @AfterAll
    static void tearDownAll() {
        HibernateUtil.shutdown();
    }

    /**
     * Очистка таблицы 'users' перед каждым тестом.
     */
    @BeforeEach
    void cleanTable() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.createMutationQuery("DELETE FROM UserEntity").executeUpdate();
            tx.commit();
        }
    }

    /**
     * Проверяет, что метод create() сохраняет пользователя в БД и присваивает ему id.
     */
    @Test
    void create_shouldPersistUserInDatabase() {
        UserEntity user = createTestUser();
        userDao.create(user);

        assertNotNull(user.getId(), "ID должен быть присвоен после сохранения");

        UserEntity found = userDao.getById(user.getId());

        assertNotNull(found, "Пользователь должен быть найден");
        assertEquals(user.getEmail(), found.getEmail());
    }

    /**
     * Проверяет, что getById() возвращает пользователя, если он существует.
     */
    @Test
    void getById_existingUser_shouldReturnUser() {
        UserEntity user = createTestUser();
        userDao.create(user);

        UserEntity found = userDao.getById(user.getId());

        assertNotNull(found);
        assertEquals(user.getName(), found.getName());
        assertEquals(user.getEmail(), found.getEmail());
    }

    /**
     * Проверяет, что getById() возвращает null, если пользователя с таким id не существует.
     */
    @Test
    void getById_nonExistingId_shouldReturnNull() {
        assertNull(userDao.getById(999));
    }

    /**
     * Проверяет, что getAll() возвращает всех пользователей, если они существуют в базе данных.
     */
    @Test
    void getAll_whenUsersExist_shouldReturnList() {
        UserEntity user1 = createTestUser();
        user1.setEmail("user1@mail.ru");

        UserEntity user2 = createTestUser();
        user2.setEmail("user2@mail.ru");

        userDao.create(user1);
        userDao.create(user2);

        List<UserEntity> users = userDao.getAll();

        assertNotNull(users);
        assertEquals(2, users.size(), "Должно быть 2 пользователя в базе");
    }


    /**
     * Проверяет, что getAll() возвращает пустой список, если нет пользователей в базе данных.
     */
    @Test
    void getAll_whenNoUsers_shouldReturnEmptyList() {
        List<UserEntity> users = userDao.getAll();
        assertNotNull(users);
        assertTrue(users.isEmpty());
    }

    /**
     * Проверяет, что метод update() корректно обновляет существующего пользователя.
     */
    @Test
    void update_shouldModifyExistingUser() {
        UserEntity user = createTestUser();
        userDao.create(user);

        String newName = "Updated Name";
        user.setName(newName);

        userDao.update(user);

        UserEntity updated = userDao.getById(user.getId());
        assertNotNull(updated);
        assertEquals(newName, updated.getName());
    }

    /**
     * Проверяет, что delete() удаляет существующего пользователя.
     */
    @Test
    void delete_existingUser_shouldRemoveFromDatabase() {
        UserEntity user = createTestUser();
        userDao.create(user);
        int id = user.getId();

        userDao.delete(id);

        UserEntity deleted = userDao.getById(id);
        assertNull(deleted);
    }

    /**
     * Проверяет, что delete() не выбрасывает исключение, если пользователя с таким id не существует.
     */
    @Test
    void delete_nonExistingUser_shouldNotThrow() {
        assertDoesNotThrow(() -> userDao.delete(9999));
    }

    /**
     * Вспомогательный метод для создания тестового пользователя.
     *
     * @return объект {@link UserEntity}.
     */
    private UserEntity createTestUser() {
        UserEntity user = new UserEntity();
        user.setName("Test User");
        user.setEmail("user@mail.ru");
        user.setAge(25);
        user.setCreatedAt(LocalDateTime.now());
        return user;
    }
}
