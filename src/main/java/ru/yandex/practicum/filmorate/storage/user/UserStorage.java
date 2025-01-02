package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    Optional<User> getUserById(long id);

    boolean isEmailAlreadyInData(String email);

    List<User> getAllUsers();

    User addUser(User user);

    User updateUser(User user);

    void removeUser(long id);

    List<User> getCommonFriends(User user, User friend);
}
