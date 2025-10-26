package dao;

import models.User;
import java.util.List;

public interface UserDao {
    void create(User user);
    User getById(int id);
    List<User> getAll();
    void update(User user);
    void delete(int id);
}
