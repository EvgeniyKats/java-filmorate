package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.DuplicateException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validate.Create;
import ru.yandex.practicum.filmorate.validate.Update;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();
    private final Set<User> usersSearch = new HashSet<>();

    @GetMapping
    public Collection<User> findAll() {
        log.info("Получен GET запрос /users");
        Collection<User> result = users.values();
        log.debug("Текущий список фильмов: {}.", result);
        return result;
    }

    @PostMapping
    public User createUser(@Validated(Create.class) @RequestBody User user) {
        log.info("Получен POST запрос /users");
        try {
            validateUser(user, false);
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
                log.trace("Пользователь не указал имя. Для отображения используется логин.");
            }
            long id = getNextId();
            log.debug("Был получен id для user: {}", id);
            user.setId(id);
            users.put(id, user);
            usersSearch.add(user);
            log.info("User {}, был добавлен в хранилище.", user);
            return user;
        } catch (ValidationException | DuplicateException e) {
            log.warn("Не удалось создать пользователя {} с ошибкой: {}.", user, e.getMessage());
            throw e;
        }
    }

    @PutMapping
    public User updateUser(@Validated(Update.class) @RequestBody User user) {
        log.info("Получен PUT запрос /users");
        try {
            validateUser(user, true);
            return mergeUserInfo(user);
        } catch (ValidationException | DuplicateException e) {
            log.warn("Не удалось обновить пользователя {} с ошибкой: {}.", user, e.getMessage());
            throw e;
        }
    }

    private void validateUser(User user, boolean update) throws ValidationException, DuplicateException {
        if (update) {
            if (!users.containsKey(user.getId())) {
                throw new ValidationException("В хранилище " + users.keySet() + ", отсутствует id: " + user.getId());
            }
            log.trace("User прошёл проверку на отсутствие id в хранилище.");
            if (user.getEmail() != null && !users.get(user.getId()).equals(user)) {
                throwDuplicateIfEmailAlreadyInBase(user);
            }
        } else {
            throwDuplicateIfEmailAlreadyInBase(user);
        }
        log.trace("User прошёл проверку на дубликат.");
    }

    private void throwDuplicateIfEmailAlreadyInBase(User user) throws DuplicateException {
        if (usersSearch.contains(user)) {
            throw new DuplicateException("Такой Email уже используется " + user.getEmail());
        }
    }

    private User mergeUserInfo(User newUser) {
        User oldUser = users.get(newUser.getId());
        if (newUser.getEmail() != null) oldUser.setEmail(newUser.getEmail());
        if (newUser.getBirthday() != null) oldUser.setBirthday(newUser.getBirthday());
        if (newUser.getLogin() != null) oldUser.setLogin(newUser.getLogin());
        if (newUser.getName() != null && !newUser.getName().isBlank()) oldUser.setName(newUser.getName());
        log.info("User {}, был обновлен в хранилище.", oldUser);
        return oldUser;
    }

    private long getNextId() {
        long id = users.keySet().stream()
                .mapToLong(value -> value)
                .max()
                .orElse(0);
        return ++id;
    }
}
