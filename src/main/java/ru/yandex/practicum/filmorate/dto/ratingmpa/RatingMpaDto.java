package ru.yandex.practicum.filmorate.dto.ratingmpa;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RatingMpaDto {
    @NotNull
    private int id;
    private String name;
}
