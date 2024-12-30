package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.FilmGenrePair;
import ru.yandex.practicum.filmorate.model.FilmLikePair;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface FilmGenresStorage {
    FilmGenrePair getGenreById(long genreId);

    List<FilmGenrePair> getGenresByFilmId(long filmId);
}
