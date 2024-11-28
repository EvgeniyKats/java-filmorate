package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FilmStorage {
    Film getFilm(long id);

    List<Film> getAllFilms();

    void addFilm(Film film);

    Optional<Film> updateFilm(Film film);

    void removeFilm(long id);

    boolean isFilmInBaseByFilm(Film film);

    boolean isFilmInBaseById(long id);
}
