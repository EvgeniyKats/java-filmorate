package ru.yandex.practicum.filmorate.model;

public enum RatingMPA {
    NO_DATA,
    G,
    PG,
    PG_13,
    R,
    NC_17;

    @Override
    public String toString() {
        return name().replace("_", "-");
    }
}
