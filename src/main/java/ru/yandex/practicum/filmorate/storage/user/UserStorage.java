package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    Optional<User> getUser(long id);

    List<User> getAllUsers();

    boolean addUser(User user);

    Optional<User> updateUser(User user);

    void removeUser(long id);
}
