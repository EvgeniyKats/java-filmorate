package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.validate.Create;
import ru.yandex.practicum.filmorate.validate.Update;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> findAll() {
        log.info("Получен GET запрос /users");
        return userService.findAll();
    }

    @PostMapping
    public User createUser(@Validated(Create.class) @RequestBody User user) {
        log.info("Получен POST запрос /users");
        return userService.createUser(user);
    }

    @PutMapping
    public Optional<User> updateUser(@Validated(Update.class) @RequestBody User user) {
        log.info("Получен PUT запрос /users");
        return userService.updateUser(user);
    }
}
