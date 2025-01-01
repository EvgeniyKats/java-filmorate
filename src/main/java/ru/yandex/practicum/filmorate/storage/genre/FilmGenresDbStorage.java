package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmGenrePair;

import java.util.List;

@Component
public class FilmGenresDbStorage implements FilmGenresStorage {
    @Override
    public FilmGenrePair getGenreById(long genreId) {
        return null;
    }

    @Override
    public List<FilmGenrePair> getGenresByFilmId(long filmId) {
        return List.of();
    }
}
