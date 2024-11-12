package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(of = "id")
@Builder
public class Film {
    public static final LocalDate MOST_EARLY_RELEASE_DATE =
            LocalDate.of(1895, 12, 28);

    private Long id;
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @NotNull
    private Integer duration;
}
