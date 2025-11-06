package dao;

import entity.UserEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.HibernateUtil;

import java.util.List;

/**
 * Реализация интерфейса {@link UserDao} с использованием ORM-фреймворка Hibernate.
 * Для каждой операции используется новая сессия Hibernate,
 * а при изменении данных -- транзакция.
 * Все операции логируются через {@link org.slf4j.Logger}.
 */
public class UserDaoImpl implements UserDao {

    private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    @Override
    public void create(UserEntity userEntity) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(userEntity);
            tx.commit();
            logger.info("Пользователь успешно создан: {}", userEntity);
        } catch (Exception e) {
            if (tx != null && tx.getStatus().canRollback()) {
                try {
                    tx.rollback();
                } catch (Exception ex) {
                    logger.warn("Не удалось выполнить rollback транзакции", ex);
                }
            }
            logger.error("Ошибка при создании пользователя: {}", userEntity, e);
        }
    }

    @Override
    public UserEntity getById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            UserEntity userEntity = session.get(UserEntity.class, id);
            if (userEntity != null) {
                logger.info("Пользователь найден по ID {}: {}", id, userEntity);
            } else {
                logger.warn("Пользователь с ID {} не найден", id);
            }

            return userEntity;
        } catch (Exception e) {
            logger.error("Ошибка при поиске по ID {}", id, e);

            return null;
        }
    }

    @Override
    public List<UserEntity> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<UserEntity> userEntities = session.createQuery("from UserEntity", UserEntity.class).list();
            logger.info("Получено {} пользователей", userEntities.size());

            return userEntities;
        } catch (Exception e) {
            logger.error("Ошибка при получении пользователей", e);

            return List.of();
        }
    }

    @Override
    public void update(UserEntity userEntity) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(userEntity);
            tx.commit();
            logger.info("Пользователь обновлён: {}", userEntity);
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            logger.error("Ошибка при обновлении: {}", userEntity, e);
        }
    }

    @Override
    public void delete(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            UserEntity userEntity = session.get(UserEntity.class, id);
            if (userEntity != null) {
                session.remove(userEntity);
                logger.info("Пользователь удалён: {}", userEntity);
            } else {
                logger.warn("Попытка удалить несуществующего пользователя с ID {}", id);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            logger.error("Ошибка при удалении с ID {}", id, e);
        }
    }
}
