package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film getFilm(long id);

    List<Film> getAllFilms();

    void addFilm(Film film);

    Film updateFilm(Film film);

    void removeFilm(long id);

    boolean isFilmInStorageById(long id);

    List<Film> getTopFilms(int count);
}
