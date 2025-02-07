package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreStorage {
    Optional<Genre> getGenre(int id);

    List<Genre> getAllGenres();

    Genre addGenre(Genre genre);

    Genre updateGenre(Genre genre);

    boolean removeGenre(int id);
}
