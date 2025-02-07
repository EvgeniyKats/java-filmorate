package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserDto;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(of = "email")
@Builder(toBuilder = true)
public class User {
    private Long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;

    public void updateFieldsFromUpdateDto(UpdateUserDto request) {
        if (request.hasEmail()) email = request.getEmail();
        if (request.hasLogin()) login = request.getLogin();
        if (request.hasName()) name = request.getName();
        if (request.hasBirthDay()) birthday = request.getBirthday();
    }
}
