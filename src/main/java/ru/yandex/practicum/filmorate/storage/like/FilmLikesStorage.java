package ru.yandex.practicum.filmorate.storage.like;

import ru.yandex.practicum.filmorate.model.FilmLikePair;

import java.util.List;

public interface FilmLikesStorage {
    FilmLikePair getPairById(long id);

    List<FilmLikePair> getFilmLikesByFilmId(long filmId);

    List<FilmLikePair> getUserLikesByUserId(long userId);
}
