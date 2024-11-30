package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    List<User> findAll();

    User createUser(User user);

    User updateUser(User user);

    User getUserById(long id);

    List<User> getFriends(Long userId);

    List<Long> addFriend(Long userId, Long friendId);

    List<Long> removeFriend(Long userId, Long friendId);

    List<User> getCommonFriends(Long userId, Long friendId);
}
