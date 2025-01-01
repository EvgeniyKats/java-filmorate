package ru.yandex.practicum.filmorate.storage.ratingmpa;

import ru.yandex.practicum.filmorate.model.RatingMpa;

public interface RatingMpaStorage {
    RatingMpa getRatingMPAById(long ratingId);

    RatingMpa getRatingMPAByFilmId(long filmId);
}
