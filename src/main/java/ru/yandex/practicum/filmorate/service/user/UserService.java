package ru.yandex.practicum.filmorate.service.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DuplicateException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Optional;

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

    public Optional<User> updateUser(User user) {
        try {
            validateUser(user, true);
            return userStorage.updateUser(user);
        } catch (ValidationException | DuplicateException e) {
            log.warn("Не удалось обновить пользователя {} с ошибкой: {}.", user, e.getMessage());
            throw e;
        }
    }

    private void validateUser(User user, boolean update) throws ValidationException, DuplicateException {
        if (update) {
            if (user.getId() == null || !userStorage.isUserInBaseById(user.getId())) {
                throw new ValidationException("В хранилище отсутствует id: " + user.getId());
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
