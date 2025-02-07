package ru.yandex.practicum.filmorate.dto.film;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.dto.genre.GenreDto;
import ru.yandex.practicum.filmorate.dto.ratingmpa.RatingMpaDto;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class FilmDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    @JsonProperty("mpa")
    private RatingMpaDto mpaDto;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY, value = "genres")
    private List<GenreDto> genresDto;
}
