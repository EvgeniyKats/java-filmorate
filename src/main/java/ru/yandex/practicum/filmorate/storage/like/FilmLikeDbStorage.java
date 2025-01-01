package ru.yandex.practicum.filmorate.storage.like;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmLikePair;

import java.util.List;

@Component
public class FilmLikeDbStorage implements  FilmLikesStorage {
    @Override
    public FilmLikePair getPairById(long id) {
        return null;
    }

    @Override
    public List<FilmLikePair> getFilmLikesByFilmId(long filmId) {
        return List.of();
    }

    @Override
    public List<FilmLikePair> getUserLikesByUserId(long userId) {
        return List.of();
    }
}
