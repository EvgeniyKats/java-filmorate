package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {
    Genre getGenre(long id);

    List<Genre> getAllGenres();

    void addGenre(Genre genre);

    Genre updateGenre(Genre genre);

    void removeGenre(long id);

    boolean isGenreInStorageById(long id);

    boolean isGenreNotExistInStorageById(long id);
}
