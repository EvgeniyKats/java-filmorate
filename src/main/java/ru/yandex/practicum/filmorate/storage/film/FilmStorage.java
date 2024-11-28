package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Optional<Film> getFilm(long id);

    List<Film> getAllFilms();

    boolean addFilm(Film film);

    Optional<Film> updateFilm(Film film);

    void removeFilm(long id);
}