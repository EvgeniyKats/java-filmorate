package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.FilmGenrePair;

import java.util.List;
import java.util.Optional;

public interface FilmGenresStorage {
    Optional<FilmGenrePair> getPairById(long id);

    List<FilmGenrePair> getGenresByFilmId(long filmId);

    void addFilmGenre(long filmId, int genreId);

    void removeFilmGenre(long filmId, int genreId);
}
