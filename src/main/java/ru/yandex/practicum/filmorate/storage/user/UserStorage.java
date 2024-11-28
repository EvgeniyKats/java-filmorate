package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User getUser(long id);

    List<User> getAllUsers();

    void addUser(User user);

    User updateUser(User user);

    void removeUser(long id);

    boolean isUserInBaseByUser(User user);

    boolean isUserInBaseById(long id);
}
