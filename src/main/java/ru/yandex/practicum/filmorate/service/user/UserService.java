package ru.yandex.practicum.filmorate.service.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.custom.DuplicateException;
import ru.yandex.practicum.filmorate.exception.custom.EntityNotExistException;
import ru.yandex.practicum.filmorate.exception.custom.NotFoundException;
import ru.yandex.practicum.filmorate.exception.custom.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

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
        try {
            validateUser(user, false);
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
                log.trace("Пользователь не указал имя. Для отображения используется логин.");
            }
            userStorage.addUser(user);
            log.info("User {}, был добавлен в хранилище.", user);
            return user;
        } catch (ValidationException | DuplicateException e) {
            log.warn("Не удалось создать пользователя {} с ошибкой: {}.", user, e.getMessage());
            throw e;
        }
    }

    public User updateUser(User user) {
        try {
            validateUser(user, true);
            return userStorage.updateUser(user);
        } catch (ValidationException | DuplicateException e) {
            log.warn("Не удалось обновить пользователя {} с ошибкой: {}.", user, e.getMessage());
            throw e;
        }
    }

    public User getUserById(long id) {
        User user = userStorage.getUser(id);
        if (user == null) throw new NotFoundException("Пользователь с id=" + id + " отсутствует в хранилище.");
        return user;
    }

    public boolean isUserInStorageById(long id) {
        return userStorage.isUserInBaseById(id);
    }

    public List<User> getFriends(Long userId) {
        User user = userStorage.getUser(userId);
        if (user == null) throw new EntityNotExistException("User", userId);
        return user.getFriendsId().stream()
                .map(userStorage::getUser)
                .toList();
    }

    public List<Long> addFriend(Long userId, Long friendId) {
        User user = userStorage.getUser(userId);
        if (user == null) throw new EntityNotExistException("User", userId);
        User friend = userStorage.getUser(friendId);
        if (friend == null) throw new EntityNotExistException("Friend", friendId);
        user.addFriend(friendId);
        friend.addFriend(userId);
        return user.getFriendsId();
    }

    public List<Long> removeFriend(Long userId, Long friendId) {
        User user = userStorage.getUser(userId);
        if (user == null) throw new EntityNotExistException("User", userId);
        User friend = userStorage.getUser(friendId);
        if (friend == null) throw new EntityNotExistException("Friend", friendId);
        user.removeFriend(friendId);
        friend.removeFriend(userId);
        return user.getFriendsId();
    }

    public List<User> getCommonFriends(Long userId, Long friendId) {
        User user = userStorage.getUser(userId);
        if (user == null) throw new EntityNotExistException("User", userId);
        User friend = userStorage.getUser(friendId);
        if (friend == null) throw new EntityNotExistException("Friend", friendId);
        return user.getFriendsId().stream()
                .filter(id -> friend.getFriendsId().contains(id))
                .map(userStorage::getUser)
                .toList();
    }

    private void validateUser(User user, boolean update) throws ValidationException, DuplicateException {
        if (update) {
            if (user.getId() == null || !userStorage.isUserInBaseById(user.getId())) {
                throw new NotFoundException("В хранилище отсутствует id: " + user.getId());
            }
            log.trace("User прошёл проверку на отсутствие id в хранилище.");
            User oldUser = userStorage.getUser(user.getId());
            if (user.getEmail() != null && !user.equals(oldUser)) {
                throwDuplicateIfEmailAlreadyInBase(user);
            }
        } else {
            throwDuplicateIfEmailAlreadyInBase(user);
        }
        log.trace("User прошёл проверку на дубликат.");
    }

    private void throwDuplicateIfEmailAlreadyInBase(User user) throws DuplicateException {
        if (userStorage.isUserInBaseByUser(user)) {
            throw new DuplicateException("Такой Email уже используется " + user.getEmail());
        }
    }
}
