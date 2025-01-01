package ru.yandex.practicum.filmorate.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateUserRequest {
    @NotNull
    @Email
    private String email;
    @NotNull
    @Pattern(regexp = "\\S+")
    private String login;
    private String name;
    @NotNull
    @Past
    private LocalDate birthday;
}
