package dao;

import entity.UserEntity;

import java.util.List;

/**
 * Интерфейс для работы с сущностью User в БД.
 * Определяет методы для выполнения CRUD операций.
 */
public interface UserDao {

    /**
     * Создаёт нового пользователя в базе данных.
     *
     * @param userEntity объект пользователя, который необходимо сохранить
     */
    void create(UserEntity userEntity);

    /**
     * Находит пользователя по идентификатору.
     *
     * @param id уникальный идентификатор.
     * @return объект {@link UserEntity}, если найден, иначе null
     */
    UserEntity getById(int id);

    /**
     * Возвращает список пользователей.
     *
     * @return список объектов {@link UserEntity}.
     * Если в базе данных нет пользователей, возвращается пустой список.
     */
    List<UserEntity> getAll();

    /**
     * Обновляет данные существуующего пользователя.
     *
     * @param userEntity объект {@link UserEntity} с обновлёнными данными, не должен быть null
     */
    void update(UserEntity userEntity);

    /**
     * Удаляет пользователя из базы данных по идентификатору.
     *
     * @param id уникальный идентификатор пользователя, которого нужно удалить.
     */
    void delete(int id);
}
