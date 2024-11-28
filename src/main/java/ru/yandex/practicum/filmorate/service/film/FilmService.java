package ru.yandex.practicum.filmorate.service.film;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DuplicateException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class FilmService {
    public static final LocalDate MOST_EARLY_RELEASE_DATE =
            LocalDate.of(1895, 12, 28);
    private final FilmStorage filmStorage;

    public List<Film> findAll() {
        List<Film> result = filmStorage.getAllFilms();
        log.debug("Текущий список фильмов: {}.", result);
        return result;
    }

    public Film createFilm(Film film) {
        try {
            validateFilm(film, false);
            filmStorage.addFilm(film);
            log.info("Film {}, был добавлен в хранилище.", film);
            return film;
        } catch (ValidationException | DuplicateException e) {
            log.warn("Не удалось создать фильм {} с ошибкой: {}.", film, e.getMessage());
            throw e;
        }
    }

    public Optional<Film> updateFilm(Film film) {
        try {
            validateFilm(film, true);
            return filmStorage.updateFilm(film);
        } catch (ValidationException | DuplicateException e) {
            log.warn("Не удалось обновить фильм {} с ошибкой: {}", film, e.getMessage());
            throw e;
        }
    }

    private void validateFilm(Film film, boolean update) throws ValidationException, DuplicateException {
        if (update) {
            if (film.getId() == null || !filmStorage.isFilmInBaseById(film.getId())) {
                throw new ValidationException("В хранилище отсутствует id: " + film.getId());
            }
            log.trace("Film прошёл проверку на отсутствие id в хранилище.");
            Film oldFilm = filmStorage.getFilm(film.getId());
            if (film.getName() != null && !(film.equals(oldFilm))) throwDuplicateIfNameAlreadyInBase(film);
        } else {
            throwDuplicateIfNameAlreadyInBase(film);
        }
        log.trace("Film прошёл проверку на дубликат.");

        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(MOST_EARLY_RELEASE_DATE)) {
            throw new ValidationException("Дата релиза раньше 28 декабря 1895 г.");
        }
        log.trace("Film прошёл проверку на дату релиза <= 28.12.1895: {}.", film.getReleaseDate());
    }

    private void throwDuplicateIfNameAlreadyInBase(Film film) throws DuplicateException {
        if (filmStorage.isFilmInBaseByFilm(film)) {
            throw new DuplicateException("Film с названием " + film.getName() + " уже содержится в базе.");
        }
    }
}
