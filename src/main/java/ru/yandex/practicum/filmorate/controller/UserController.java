package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.user.CreateUserDto;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserDto;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> findAll() {
        log.info("Получен GET запрос /users");
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        log.info("Получен GET запрос /users/{}", id);
        return userService.getUserById(id);
    }

    @GetMapping("/{id}/friends")
    public List<UserDto> getFriends(@PathVariable Long id) {
        log.info("Получен GET запрос /users/{}/friends", id);
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<UserDto> getCommonFriends(@PathVariable Long id,
                                       @PathVariable Long otherId) {
        log.info("Получен GET запрос /users/{}/friends/common/{}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Validated @RequestBody CreateUserDto request) {
        log.info("Получен POST запрос /users");
        return userService.createUser(request);
    }

    @PutMapping
    public UserDto updateUser(@Validated @RequestBody UpdateUserDto request) {
        log.info("Получен PUT запрос /users");
        return userService.updateUser(request);
    }

    @PutMapping("/{id}/friends/{friendId}")
    List<Long> addFriend(@PathVariable Long id,
                          @PathVariable Long friendId) {
        log.info("Получен PUT запрос /users/{}/friends/{}", id, friendId);
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    List<Long> removeFriend(@PathVariable Long id,
                          @PathVariable Long friendId) {
        log.info("Получен DELETE запрос /users/{}/friends/{}", id, friendId);
        return userService.removeFriend(id, friendId);
    }
}
