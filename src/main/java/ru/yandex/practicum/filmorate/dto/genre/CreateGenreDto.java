package ru.yandex.practicum.filmorate.dto.genre;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateGenreDto {
    @NotBlank
    private String name;
}
