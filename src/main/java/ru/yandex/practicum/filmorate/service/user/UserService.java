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

    public boolean isUserInStorageById(long id) {
        return userStorage.isUserInBaseById(id);
    }

    public List<Long> getFriends(User user) {
        if (!userStorage.isUserInBaseById(user.getId())) throw new EntityNotExistException("User", user.getId());
        return user.getFriends();
    }

    public List<Long> addFriend(User user, User friend) {
        throwIfNoInBase(user, friend);
        user.addFriend(friend.getId());
        friend.addFriend(user.getId());
        return user.getFriends();
    }

    public List<Long> removeFriend(User user, User friend) {
        throwIfNoInBase(user, friend);
        user.removeFriend(friend.getId());
        friend.removeFriend(user.getId());
        return user.getFriends();
    }

    public List<Long> getCommonFriends(User user, User friend) {
        throwIfNoInBase(user, friend);
        return user.getFriends().stream()
                .filter(id -> friend.getFriends().contains(id))
                .toList();
    }

    private void throwIfNoInBase(User user, User friend) {
        if (!userStorage.isUserInBaseById(user.getId())) throw new EntityNotExistException("User", user.getId());
        if (!userStorage.isUserInBaseById(friend.getId())) throw new EntityNotExistException("Friend", friend.getId());
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
