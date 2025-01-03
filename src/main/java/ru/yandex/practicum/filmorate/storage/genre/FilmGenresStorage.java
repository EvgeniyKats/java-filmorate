package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.FilmGenrePair;

import java.util.List;
import java.util.Optional;

public interface FilmGenresStorage {
    Optional<FilmGenrePair> getPairById(long id);

    List<FilmGenrePair> getGenresByFilmId(long filmId);

    void addGenreToFilm(long filmId, int genreId);

    void removeGenreOfFilm(long filmId, int genreId);
}
