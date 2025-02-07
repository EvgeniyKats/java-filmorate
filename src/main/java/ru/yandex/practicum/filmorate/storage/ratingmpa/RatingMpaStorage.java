package ru.yandex.practicum.filmorate.storage.ratingmpa;

import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.util.List;
import java.util.Optional;

public interface RatingMpaStorage {

    Optional<RatingMpa> getRatingMPAById(long ratingId);

    List<RatingMpa> getAll();
}
