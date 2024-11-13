package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.DuplicateException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        log.info("Получен GET запрос /users");
        Collection<User> result = users.values();
        log.debug("Текущий список фильмов: {}.", result);
        return result;
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("Получен POST запрос /users");
        try {
            validateUser(user, false);
            long id = getNextId();
            log.debug("Был получен id для user: {}", id);
            user.setId(id);
            users.put(id, user);
            log.info("User {}, был добавлен в хранилище.", user);
            return user;
        } catch (ValidationException | DuplicateException e) {
            log.warn("Не удалось создать пользователя {} с ошибкой: {}.", user, e.getMessage());
            throw e;
        }
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Получен PUT запрос /users");
        try {
            validateUser(user, true);
            users.put(user.getId(), user);
            log.info("User {}, был обновлен в хранилище.", user);
            return user;
        } catch (ValidationException | DuplicateException e) {
            log.warn("Не удалось обновить пользователя {} с ошибкой: {}.", user, e.getMessage());
            throw e;
        }
    }

    private void validateUser(User user, boolean update) throws ValidationException, DuplicateException {
        if (update) {
            if (user.getId() == null) throw new ValidationException("user.getId() == null");
            log.trace("User прошёл проверку на id == null");

            if (!users.containsKey(user.getId())) {
                throw new ValidationException("В хранилище " + users.keySet() + ", отсутствует id: " + user.getId());
            }
            log.trace("User прошёл проверку на отсутствие id в хранилище.");
            User oldUser = users.get(user.getId());
            if (!oldUser.getEmail().equals(user.getEmail())) {
                for (User u : users.values()) {
                    if (u.getEmail().equals(user.getEmail())) {
                        throw new DuplicateException("Такой Email уже используется");
                    }
                }
            }
        } else {
            for (User u : users.values()) {
                if (u.getEmail().equals(user.getEmail())) throw new DuplicateException("Такой Email уже используется");
            }
        }
        log.trace("User прошёл проверку на дубликат.");

        if (user.getLogin().contains(" ")) throw new ValidationException("Логин содержит пробелы.");
        log.trace("User прошёл проверку на пробелы в логине {}.", user.getLogin());

        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        log.trace("User прошёл проверку на дату рождения в будущем {}.", user.getBirthday());

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("User не указал имя, для отображения установлен логин.");
        }
    }

    private long getNextId() {
        long id = users.keySet().stream()
                .mapToLong(value -> value)
                .max()
                .orElse(0);
        return ++id;
    }
}
