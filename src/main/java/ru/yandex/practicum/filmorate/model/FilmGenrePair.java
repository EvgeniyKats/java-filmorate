package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class FilmGenrePair {
    private long id;
    private long filmId;
    private int genreId;
}
