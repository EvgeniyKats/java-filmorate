package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.validate.Create;
import ru.yandex.practicum.filmorate.validate.Update;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(of = "email")
@Builder(toBuilder = true)
public class User {
    @NotNull(groups = Update.class)
    private Long id;
    @NotNull(groups = Create.class)
    @Email(groups = {Create.class, Update.class})
    private String email;
    @NotNull(groups = Create.class)
    @Pattern(regexp = "\\S+", groups = {Create.class, Update.class})
    private String login;
    private String name;
    @NotNull(groups = Create.class)
    @Past(groups = {Create.class, Update.class})
    private LocalDate birthday;
}
