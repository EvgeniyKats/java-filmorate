package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.custom.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> filmsData;
    private final Set<Film> filmsSearch;

    public InMemoryFilmStorage() {
        filmsData = new HashMap<>();
        filmsSearch = new HashSet<>();
    }

    @Override
    public Film getFilm(long id) {
        return filmsData.get(id);
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(filmsData.values());
    }

    @Override
    public void addFilm(Film film) {
        long id = getNextId();
        log.debug("Был получен id для film: {}", id);
        film.setId(id);
        filmsData.put(film.getId(), film);
        filmsSearch.add(film);
    }

    @Override
    public Film updateFilm(Film newFilm) {
        Film oldFilm = filmsData.get(newFilm.getId());
        if (oldFilm == null) {
            log.info("Film {}, отсутствует в хранилище и не был обновлен.", newFilm.getId());
            throw new NotFoundException("Фильм id=" + newFilm.getId() + "отсутствует в хранилище.");
        }
        if (newFilm.getName() != null) oldFilm.setName(newFilm.getName());
        if (newFilm.getDescription() != null) oldFilm.setDescription(newFilm.getDescription());
        if (newFilm.getReleaseDate() != null) oldFilm.setReleaseDate(newFilm.getReleaseDate());
        if (newFilm.getDuration() != null) oldFilm.setDuration(newFilm.getDuration());
        log.info("Film {}, был обновлен в хранилище.", oldFilm);
        return oldFilm;
    }

    @Override
    public void removeFilm(long id) {
        Film film = filmsData.remove(id);
        filmsSearch.remove(film);
    }

    @Override
    public boolean isFilmInBaseByFilm(Film film) {
        return filmsSearch.contains(film);
    }

    @Override
    public boolean isFilmInBaseById(long id) {
        return filmsData.containsKey(id);
    }

    private long getNextId() {
        long id = filmsData.keySet().stream()
                .mapToLong(value -> value)
                .max()
                .orElse(0);
        return ++id;
    }
}
