package ru.yandex.practicum.filmorate.model;

public enum Genre {
    COMEDY,
    DRAMA,
    CARTOON,
    THRILLER,
    DOCUMENTARY,
    ACTION_MOVIE;

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).replace("_", " ").toLowerCase();
    }
}
