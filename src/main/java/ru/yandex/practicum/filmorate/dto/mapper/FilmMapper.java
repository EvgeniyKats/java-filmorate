package ru.yandex.practicum.filmorate.dto.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.film.CreateFilmDto;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.genre.GenreDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilmMapper {

    public static Film mapToFilm(CreateFilmDto request) {
        Film result = Film.builder()
                .name(request.getName())
                .description(request.getDescription())
                .releaseDate(request.getReleaseDate())
                .duration(request.getDuration())
                .mpaId(RatingMpaMapper.mapToRatingMpa(request.getMpaDto()))
                .build();
        if (request.getGenresDto() != null) {
            request.getGenresDto().stream()
                    .map(GenreMapper::mapToGenre)
                    .forEach(result::addGenre);
        }
        return result;
    }

    public static FilmDto mapToFilmDto(Film film, Map<Integer, RatingMpa> allMpa) {
        List<GenreDto> genresDto = film.getGenres().stream()
                .map(GenreMapper::mapToGenreDto)
                .toList();
        return FilmDto.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .mpaDto(RatingMpaMapper.mapToRatingMpaDto(allMpa.get(film.getMpaId())))
                .genresDto(genresDto)
                .build();
    }
}
