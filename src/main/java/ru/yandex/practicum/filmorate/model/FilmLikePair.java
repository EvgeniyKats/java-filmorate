package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class FilmLikePair {
    private long id;
    private long filmId;
    private long userId;
}
