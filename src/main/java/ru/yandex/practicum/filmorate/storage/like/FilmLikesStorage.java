package ru.yandex.practicum.filmorate.storage.like;

import ru.yandex.practicum.filmorate.model.FilmLikePair;

import java.util.List;
import java.util.Optional;

public interface FilmLikesStorage {
    Optional<FilmLikePair> getPairById(long id);

    List<FilmLikePair> getFilmLikesByFilmId(long filmId);

    List<FilmLikePair> getUserLikesByUserId(long userId);

    void addLike(long filmId, long userId);

    void removeLike(long filmId, long userId);
}
