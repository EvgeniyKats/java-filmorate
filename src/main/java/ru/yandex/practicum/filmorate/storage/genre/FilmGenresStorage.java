package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.FilmGenrePair;

import java.util.List;
import java.util.Optional;

public interface FilmGenresStorage {
    Optional<FilmGenrePair> getPairById(long id);

    List<FilmGenrePair> getAllPairs();

    List<FilmGenrePair> getPairsOfFilmById(long filmId);

    List<FilmGenrePair> getPairsByListOfFilmId(List<Long> filmsIds);

    void addGenreToFilm(long filmId, int genreId);

    void removeGenreOfFilm(long filmId, int genreId);
}
