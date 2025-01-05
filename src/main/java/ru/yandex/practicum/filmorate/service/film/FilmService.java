package ru.yandex.practicum.filmorate.service.film;


import ru.yandex.practicum.filmorate.dto.film.CreateFilmDto;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmDto;

import java.util.List;

public interface FilmService {
    List<FilmDto> findAll();

    FilmDto createFilm(CreateFilmDto request);

    FilmDto updateFilm(UpdateFilmDto request);

    FilmDto getFilmById(long id);

    List<FilmDto> getTopFilms(Integer count);

    List<Long> addFilmLike(Long filmId, Long userId);

    List<Long> removeFilmLike(Long filmId, Long userId);
}
