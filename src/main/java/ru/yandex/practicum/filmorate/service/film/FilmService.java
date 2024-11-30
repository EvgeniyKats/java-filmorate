package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    List<Film> findAll();

    Film createFilm(Film film);

    Film updateFilm(Film film);

    Film getFilmById(long id);

    List<Film> getTopFilms(Integer count);

    List<Long> addFilmLike(Long filmId, Long userId);

    List<Long> removeFilmLike(Long filmId, Long userId);
}
