package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.film.CreateFilmDto;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmDto;
import ru.yandex.practicum.filmorate.dto.genre.GenreDto;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public class FilmMapper {

    private FilmMapper() {
    }

    public static Film mapToFilm(CreateFilmDto request) {
        Film result = Film.builder()
                .name(request.getName())
                .description(request.getDescription())
                .releaseDate(request.getReleaseDate())
                .duration(request.getDuration())
                .mpa(RatingMpaMapper.mapToRatingMpa(request.getMpaDto()))
                .build();
        if (request.getGenresDto() != null) {
            request.getGenresDto().stream()
                    .map(GenreMapper::mapToGenre)
                    .forEach(result::addGenre);
        }
        return result;
    }

    public static FilmDto mapToFilmDto(Film film) {
        List<GenreDto> genresDto = film.getGenres().stream()
                .map(GenreMapper::mapToGenreDto)
                .toList();
        return FilmDto.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .mpaDto(RatingMpaMapper.mapToRatingMpaDto(film.getMpa()))
                .genresDto(genresDto)
                .build();
    }

    public static Film updateFilmFields(Film film, UpdateFilmDto request) {
        film.setId(request.getId());
        if (request.hasName()) film.setName(request.getName());
        if (request.hasDescription()) film.setDescription(request.getDescription());
        if (request.hasReleaseDate()) film.setReleaseDate(request.getReleaseDate());
        if (request.hasDuration()) film.setDuration(request.getDuration());
        if (request.hasRatingMpa()) film.setMpa(RatingMpaMapper.mapToRatingMpa(request.getMpaDto()));
        if (request.hasGenres()) {
            request.getGenresDto().stream()
                    .map(GenreMapper::mapToGenre)
                    .forEach(film::addGenre);
        }
        return film;
    }
}
