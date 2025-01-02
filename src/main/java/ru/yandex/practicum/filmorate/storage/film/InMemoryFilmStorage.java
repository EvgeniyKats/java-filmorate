package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component("inMemoryFilmStorage")
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
    public List<Film> getTopFilms(int count) {
        return filmsData.values().stream()
                .sorted((a, b) -> b.getFilmLikesByUserId().size() - a.getFilmLikesByUserId().size())
                .limit(count)
                .toList();
    }

    @Override
    public Film addFilm(Film film) {
        long id = getNextId();
        log.debug("Был получен id для film: {}", id);
        film.setId(id);
        filmsData.put(film.getId(), film);
        log.trace("Фильм {} добавлен в хранилище.", id);
        return film;
    }

    @Override
    public Film updateFilm(Film newFilm) {
        Film oldFilm = filmsData.get(newFilm.getId());
        if (newFilm.getName() != null) oldFilm.setName(newFilm.getName());
        if (newFilm.getDescription() != null) oldFilm.setDescription(newFilm.getDescription());
        if (newFilm.getReleaseDate() != null) oldFilm.setReleaseDate(newFilm.getReleaseDate());
        if (newFilm.getDuration() != null) oldFilm.setDuration(newFilm.getDuration());
        log.info("Film {}, был обновлен в хранилище.", oldFilm);
        return oldFilm;
    }

    @Override
    public void removeFilm(long id) {
        filmsData.remove(id);
        log.trace("Фильм {} удалён из хранилища.", id);
    }

    private long getNextId() {
        long id = filmsData.keySet().stream()
                .mapToLong(value -> value)
                .max()
                .orElse(0);
        return ++id;
    }
}
