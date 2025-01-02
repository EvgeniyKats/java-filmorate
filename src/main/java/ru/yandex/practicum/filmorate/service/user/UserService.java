package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.dto.user.CreateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> findAll();

    UserDto createUser(CreateUserRequest request);

    UserDto updateUser(UpdateUserRequest request);

    UserDto getUserById(long id);

    List<UserDto> getFriends(Long userId);

    List<Long> addFriend(Long userId, Long friendId);

    List<Long> removeFriend(Long userId, Long friendId);

    List<UserDto> getCommonFriends(Long userId, Long friendId);
}
