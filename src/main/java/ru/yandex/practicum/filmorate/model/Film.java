package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmDto;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.mapper.RatingMpaMapper;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Builder(toBuilder = true)
public class Film {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private RatingMpa mpa;
    private final Set<Genre> genres = new LinkedHashSet<>();

    public void addGenre(Genre genre) {
        genres.add(genre);
    }

    public void updateFieldsFromUpdateDto(UpdateFilmDto request) {
        id = (request.getId());
        if (request.hasName()) name = request.getName();
        if (request.hasDescription()) description = request.getDescription();
        if (request.hasReleaseDate()) releaseDate = request.getReleaseDate();
        if (request.hasDuration()) duration = request.getDuration();
        if (request.hasRatingMpa()) mpa = RatingMpaMapper.mapToRatingMpa(request.getMpaDto());
        if (request.hasGenres()) {
            request.getGenresDto().stream()
                    .map(GenreMapper::mapToGenre)
                    .forEach(genres::add);
        }
    }
}
