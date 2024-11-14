package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.DuplicateException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validated.Create;
import ru.yandex.practicum.filmorate.validated.Update;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    public static final LocalDate MOST_EARLY_RELEASE_DATE =
            LocalDate.of(1895, 12, 28);
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Получен GET запрос /films");
        Collection<Film> result = films.values();
        log.debug("Текущий список фильмов: {}.", result);
        return result;
    }

    @PostMapping
    public Film createFilm(@Validated(Create.class) @RequestBody Film film) {
        log.info("Получен POST запрос /films");
        try {
            validateFilm(film, false);
            long id = getNextId();
            log.debug("Был получен id для film: {}", id);
            film.setId(id);
            films.put(id, film);
            log.info("Film {}, был добавлен в хранилище.", film);
            return film;
        } catch (ValidationException | DuplicateException e) {
            log.warn("Не удалось создать фильм {} с ошибкой: {}.", film, e.getMessage());
            throw e;
        }
    }

    @PutMapping
    public Film updateFilm(@Validated(Update.class) @RequestBody Film film) {
        log.info("Получен PUT запрос /films");
        try {
            validateFilm(film, true);
            return mergeFilmInfo(film);
        } catch (ValidationException | DuplicateException e) {
            log.warn("Не удалось обновить фильм {} с ошибкой: {}", film, e.getMessage());
            throw e;
        }
    }

    private void validateFilm(Film film, boolean update) throws ValidationException, DuplicateException {
        if (update) {
            if (!films.containsKey(film.getId())) {
                throw new ValidationException("В хранилище " + films.keySet() + ", отсутствует id: " + film.getId());
            }
            log.trace("Film прошёл проверку на отсутствие id в хранилище.");
            if (!films.get(film.getId()).getName().equals(film.getName())) {
                throwDuplicateIfNameAlreadyInBase(film.getName());
            }
        } else {
            throwDuplicateIfNameAlreadyInBase(film.getName());
        }
        log.trace("Film прошёл проверку на дубликат.");

        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(MOST_EARLY_RELEASE_DATE)) {
            throw new ValidationException("Дата релиза раньше 28 декабря 1895 г.");
        }
        log.trace("Film прошёл проверку на дату релиза <= 28.12.1895: {}.", film.getReleaseDate());
    }

    private void throwDuplicateIfNameAlreadyInBase(String name) throws DuplicateException {
        for (Film f : films.values()) {
            if (f.getName().equals(name)) {
                throw new DuplicateException("Film с названием " + name + " уже содержится в базе.");
            }
        }
    }

    private Film mergeFilmInfo(Film newFilm) {
        Film oldFilm = films.get(newFilm.getId());
        if (newFilm.getName() != null) oldFilm.setName(newFilm.getName());
        if (newFilm.getDescription() != null) oldFilm.setDescription(newFilm.getDescription());
        if (newFilm.getReleaseDate() != null) oldFilm.setReleaseDate(newFilm.getReleaseDate());
        if (newFilm.getDuration() != null) oldFilm.setDuration(newFilm.getDuration());
        log.info("Film {}, был обновлен в хранилище.", oldFilm);
        return oldFilm;
    }

    private long getNextId() {
        long id = films.keySet().stream()
                .mapToLong(value -> value)
                .max()
                .orElse(0);
        return ++id;
    }
}
