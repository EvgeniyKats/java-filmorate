package ru.yandex.practicum.filmorate.service.film;


import ru.yandex.practicum.filmorate.dto.film.CreateFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;

import java.util.List;

public interface FilmService {
    List<FilmDto> findAll();

    FilmDto createFilm(CreateFilmRequest request);

    FilmDto updateFilm(UpdateFilmRequest request);

    FilmDto getFilmById(long id);

    List<FilmDto> getTopFilms(Integer count);

    List<Long> addFilmLike(Long filmId, Long userId);

    List<Long> removeFilmLike(Long filmId, Long userId);
}
