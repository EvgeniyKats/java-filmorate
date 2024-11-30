package ru.yandex.practicum.filmorate.service.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.custom.DuplicateException;
import ru.yandex.practicum.filmorate.exception.custom.EntityNotExistException;
import ru.yandex.practicum.filmorate.exception.custom.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public List<User> findAll() {
        List<User> result = userStorage.getAllUsers();
        log.debug("Текущий список пользователей: {}.", result);
        return result;
    }

    public User createUser(User user) {
        throwDuplicateIfEmailAlreadyInStorage(user, false);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.trace("Пользователь не указал имя. Для отображения используется логин.");
        }
        userStorage.addUser(user);
        log.info("User {}, был добавлен в хранилище.", user);
        return user;
    }

    public User updateUser(User user) {
        throwNotFoundIfUserAbsentInStorage(user);
        throwDuplicateIfEmailAlreadyInStorage(user, true);
        return userStorage.updateUser(user);
    }

    public User getUserById(long id) {
        User user = userStorage.getUser(id);
        if (user == null) throw new NotFoundException("Пользователь с id=" + id + " отсутствует в хранилище.");
        log.trace("Пользователь прошёл проверку на null, getUserById().");
        return user;
    }

    public boolean isUserNotExistInStorageById(long id) {
        return !userStorage.isUserInBaseById(id);
    }

    public List<User> getFriends(Long userId) {
        User user = userStorage.getUser(userId);
        if (user == null) throw new EntityNotExistException("User", userId);
        log.trace("Пользователь прошёл проверку на null, getFriends().");
        return user.getFriendsId().stream()
                .map(userStorage::getUser)
                .toList();
    }

    public List<Long> addFriend(Long userId, Long friendId) {
        User user = userStorage.getUser(userId);
        if (user == null) throw new EntityNotExistException("User", userId);
        User friend = userStorage.getUser(friendId);
        if (friend == null) throw new EntityNotExistException("Friend", friendId);
        log.trace("User, friend прошли проверку addFriend().");
        user.addFriend(friendId);
        friend.addFriend(userId);
        log.trace("Успешное добавление в друзья.");
        return user.getFriendsId();
    }

    public List<Long> removeFriend(Long userId, Long friendId) {
        User user = userStorage.getUser(userId);
        if (user == null) throw new EntityNotExistException("User", userId);
        User friend = userStorage.getUser(friendId);
        if (friend == null) throw new EntityNotExistException("Friend", friendId);
        log.trace("User, friend прошли проверку removeFriend().");
        user.removeFriend(friendId);
        friend.removeFriend(userId);
        log.trace("Успешное удаление из друзей.");
        return user.getFriendsId();
    }

    public List<User> getCommonFriends(Long userId, Long friendId) {
        User user = userStorage.getUser(userId);
        if (user == null) throw new EntityNotExistException("User", userId);
        User friend = userStorage.getUser(friendId);
        if (friend == null) throw new EntityNotExistException("Friend", friendId);
        log.trace("User, friend прошли проверку getCommonFriends().");
        Set<Long> friendsIdOfFriend = new HashSet<>(friend.getFriendsId());
        return user.getFriendsId().stream()
                .filter(friendsIdOfFriend::contains)
                .map(userStorage::getUser)
                .toList();
    }

    private void throwNotFoundIfUserAbsentInStorage(User user) {
        if (user.getId() == null || !userStorage.isUserInBaseById(user.getId())) {
            throw new NotFoundException("В хранилище отсутствует id: " + user.getId());
        }
        log.trace("User прошёл проверку на отсутствие id в хранилище.");
    }

    private void throwDuplicateIfEmailAlreadyInStorage(User user, boolean update) {
        if (!update || user.getEmail() != null && !user.equals(userStorage.getUser(user.getId()))) {
            if (userStorage.isUserInBaseByUser(user)) {
                throw new DuplicateException("Такой Email уже используется " + user.getEmail());
            }
        }
        log.trace("User прошёл проверку на дубликат.");
    }
}
