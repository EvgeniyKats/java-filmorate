package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.film.CreateFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.model.Film;

public class FilmMapper {

    private FilmMapper() {
    }

    public static Film mapToFilm(CreateFilmRequest request) {
        return Film.builder()
                .name(request.getName())
                .description(request.getDescription())
                .releaseDate(request.getReleaseDate())
                .duration(request.getDuration())
                .ratingMpa(request.getRatingMpa())
                .build();
    }

    public static FilmDto mapToFilmDto(Film film) {
        return FilmDto.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .ratingMpa(film.getRatingMpa())
                .build();
    }

    public static Film updateFilmFields(Film film, UpdateFilmRequest request) {
        film.setId(request.getId());
        if (request.hasName()) film.setName(request.getName());
        if (request.hasDescription()) film.setDescription(request.getDescription());
        if (request.hasReleaseDate()) film.setReleaseDate(request.getReleaseDate());
        if (request.hasDuration()) film.setDuration(request.getDuration());
        if (request.hasRatingMpa()) film.setRatingMpa(request.getRatingMpa());
        return film;
    }
}
