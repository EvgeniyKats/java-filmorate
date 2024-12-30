package ru.yandex.practicum.filmorate.storage.ratingMPA;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.RatingMPA;

import java.util.Optional;

public interface RatingMPAStorage {
    RatingMPA getRatingMPAById(long ratingId);

    RatingMPA getRatingMPAByFilmId(long filmId);
}
