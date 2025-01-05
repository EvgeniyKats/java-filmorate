package ru.yandex.practicum.filmorate.dto.film;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.dto.genre.GenreDto;
import ru.yandex.practicum.filmorate.dto.ratingmpa.RatingMpaDto;

import java.time.LocalDate;
import java.util.List;

@Data
public class UpdateFilmDto {
    @NotNull
    private Long id;
    private String name;
    @Size(max = 200)
    private String description;
    private LocalDate releaseDate;
    @Positive
    private Integer duration;
    @JsonProperty("mpa")
    private RatingMpaDto mpaDto;
    @JsonProperty("genres")
    private List<GenreDto> genresDto;

    public boolean hasName() {
        return name != null && !name.isBlank();
    }

    public boolean hasDescription() {
        return description != null && !description.isBlank();
    }

    public boolean hasReleaseDate() {
        return releaseDate != null;
    }

    public boolean hasDuration() {
        return duration != null;
    }

    public boolean hasRatingMpa() {
        return mpaDto != null;
    }

    public boolean hasGenres() {
        return genresDto != null;
    }
}
