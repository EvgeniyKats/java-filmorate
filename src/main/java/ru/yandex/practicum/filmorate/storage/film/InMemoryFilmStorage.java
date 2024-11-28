package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> filmsData;

    public InMemoryFilmStorage() {
        filmsData = new HashMap<>();
    }

    @Override
    public Optional<Film> getFilm(long id) {
        return Optional.ofNullable(filmsData.get(id));
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(filmsData.values());
    }

    @Override
    public boolean addFilm(Film film) {
        if (filmsData.containsKey(film.getId())) return false;
        filmsData.put(film.getId(), film);
        return true;
    }

    @Override
    public Optional<Film> updateFilm(Film film) {
        if (!filmsData.containsKey(film.getId())) return Optional.empty();
        return Optional.ofNullable(filmsData.put(film.getId(), film));
    }

    @Override
    public void removeFilm(long id) {
        filmsData.remove(id);
    }
}
