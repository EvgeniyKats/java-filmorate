package ru.yandex.practicum.filmorate.dto.film;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class FilmDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private RatingMpa ratingMpa;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final Set<Long> filmLikesByUserId = new HashSet<>();
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final Set<Long> genres = new HashSet<>();
}
