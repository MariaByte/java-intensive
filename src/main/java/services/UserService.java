package services;

import dao.UserDao;
import dao.UserDaoImpl;
import entity.UserEntity;

import java.util.List;

public class UserService {

    private final UserDao userDao;

    public UserService() {
        this.userDao = new UserDaoImpl();
    }

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void createUser(String name, String email, int age) {
        validateUserData(name, email, age);
        userDao.create(new UserEntity(name, email, age));
    }

    public UserEntity getUserById(int id) {
        return userDao.getById(id);
    }

    public List<UserEntity> getAllUsers() {
        return userDao.getAll();
    }

    public void updateUser(int id, String name, String email, int age) {
        UserEntity existingUser = userDao.getById(id);
        if (existingUser == null) {
            throw new IllegalArgumentException("Пользователь с ID " + id + " не найден");
        }

        validateUserData(name, email, age);
        existingUser.setName(name);
        existingUser.setEmail(email);
        existingUser.setAge(age);

        userDao.update(existingUser);
    }

    public void deleteUser(int id) {
        userDao.delete(id);
    }

    private  void validateUserData(String name, String email, int age) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя пользователя не может быть пустым");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email не может быть пустым");
        }
        if (age < 0) {
            throw new IllegalArgumentException("Возраст не может быть отрицательным");
        }
    }
}
