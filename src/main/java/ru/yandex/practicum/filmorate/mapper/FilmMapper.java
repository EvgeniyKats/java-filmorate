package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.film.CreateFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.model.Film;

public class FilmMapper {

    private FilmMapper() {
    }

    public static Film mapToFilm(CreateFilmRequest request) {
        Film result = Film.builder()
                .name(request.getName())
                .description(request.getDescription())
                .releaseDate(request.getReleaseDate())
                .duration(request.getDuration())
                .mpa(request.getMpa())
                .build();
        if (request.getGenres() != null) request.getGenres().forEach(result::addGenre);
        return result;
    }

    public static FilmDto mapToFilmDto(Film film) {
        return FilmDto.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .mpa(film.getMpa())
                .genres(film.getGenres())
                .build();
    }

    public static Film updateFilmFields(Film film, UpdateFilmRequest request) {
        film.setId(request.getId());
        if (request.hasName()) film.setName(request.getName());
        if (request.hasDescription()) film.setDescription(request.getDescription());
        if (request.hasReleaseDate()) film.setReleaseDate(request.getReleaseDate());
        if (request.hasDuration()) film.setDuration(request.getDuration());
        if (request.hasRatingMpa()) film.setMpa(request.getMpa());
        if (request.hasGenres()) request.getGenres().forEach(film::addGenre);
        return film;
    }
}
