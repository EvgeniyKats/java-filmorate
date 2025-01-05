package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.user.CreateUserDto;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.model.User;

public class UserMapper {

    private UserMapper() {
    }

    public static User mapToUser(CreateUserDto request) {
        return User.builder()
                .email(request.getEmail())
                .login(request.getLogin())
                .name(request.getName())
                .birthday(request.getBirthday())
                .build();
    }

    public static UserDto mapToUserDto(User user) {
        UserDto result = new UserDto();
        result.setId(user.getId());
        result.setEmail(user.getEmail());
        result.setLogin(user.getLogin());
        result.setName(user.getName());
        result.setBirthday(user.getBirthday());
        return result;
    }
}
