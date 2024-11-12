package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.DuplicateException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Получен GET запрос /films");
        Collection<Film> result = films.values();
        log.debug("Текущий список фильмов: {}.", result);
        return result;
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
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
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Получен PUT запрос /films");
        try {
            validateFilm(film, true);
            films.put(film.getId(), film);
            log.info("Film {}, был обновлен в хранилище.", film);
            return film;
        } catch (ValidationException | DuplicateException e) {
            log.warn("Не удалось обновить фильм {} с ошибкой: {}", film, e.getMessage());
            throw e;
        }
    }

    private void validateFilm(Film film, boolean update) throws ValidationException, DuplicateException {
        if (update) {
            if (film.getId() == null) throw new ValidationException("film.getId() == null");
            log.trace("Film прошёл проверку на id == null");

            if (!films.containsKey(film.getId())) {
                throw new ValidationException("В хранилище " + films.keySet() + ", отсутствует id: " + film.getId());
            }
            log.trace("Film прошёл проверку на отсутствие id в хранилище.");
            Film oldFilm = films.get(film.getId());
            if (!oldFilm.getName().equals(film.getName())) {
                for (Film f : films.values()) {
                    if (f.getName().equals(film.getName())) {
                        throw new DuplicateException("Film с названием " + film.getName() + " уже содержится в базе.");
                    }
                }
            }
        } else {
            for (Film f : films.values()) {
                if (f.getName().equals(film.getName())) {
                    throw new DuplicateException("Film с названием " + film.getName() + " уже содержится в базе.");
                }
            }
        }
        log.trace("Film прошёл проверку на дубликат.");

        if (film.getDescription().length() > 200) {
            throw new ValidationException("Описание фильма превышает 200 символов.");
        }
        log.trace("Film прошёл проверку <= 200 символов в description: {}", film.getDescription().length());

        if (film.getReleaseDate().isBefore(Film.MOST_EARLY_RELEASE_DATE)) {
            throw new ValidationException("Дата релиза раньше 28 декабря 1895 г.");
        }
        log.trace("Film прошёл проверку на дату релиза <= 28.12.1895: {}.", film.getReleaseDate());

        if (film.getDuration() < 0) {
            throw new ValidationException("Продолжительность фильма отрицательная.");
        }
        log.trace("Film прошёл проверку на отрицательную продолжительность: {}.", film.getDuration());
    }

    private long getNextId() {
        long id = films.keySet().stream()
                .mapToLong(value -> value)
                .max()
                .orElse(0);
        return ++id;
    }
}
