package ru.yandex.practicum.filmorate.service.film;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.custom.EntityNotExistException;
import ru.yandex.practicum.filmorate.exception.custom.NotFoundException;
import ru.yandex.practicum.filmorate.exception.custom.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class FilmServiceImplement implements FilmService {
    public static final LocalDate MOST_EARLY_RELEASE_DATE =
            LocalDate.of(1895, 12, 28);
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Override
    public List<Film> findAll() {
        List<Film> result = filmStorage.getAllFilms();
        log.debug("Текущий список фильмов: {}.", result);
        return result;
    }

    @Override
    public Film createFilm(Film film) {
        validateRelease(film);
        filmStorage.addFilm(film);
        log.info("Film {}, был добавлен в хранилище.", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        throwNotFoundIfFilmAbsentInStorage(film);
        validateRelease(film);
        return filmStorage.updateFilm(film);
    }

    @Override
    public Film getFilmById(long id) {
        Film film = filmStorage.getFilm(id);
        if (film == null) throw new NotFoundException("Фильм с id=" + id + " отсутствует в хранилище.");
        log.trace("Фильм прошел проверку на null.");
        return film;
    }

    @Override
    public List<Film> getTopFilms(Integer count) {
        log.trace("Параметр count прошёл проверки на корректность.");
        return filmStorage.getTopFilms(count);
    }

    @Override
    public List<Long> addFilmLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilm(filmId);
        if (film == null) throw new EntityNotExistException("Film", filmId);
        if (userStorage.isUserNotExistInStorageById(userId)) throw new EntityNotExistException("User", userId);
        film.addLike(userId);
        log.info("Лайк был успешно поставлен Film: {}, User: {}", filmId, userId);
        return film.getFilmLikesByUserId();
    }

    @Override
    public List<Long> removeFilmLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilm(filmId);
        if (film == null) throw new EntityNotExistException("Film", filmId);
        if (userStorage.isUserNotExistInStorageById(userId)) throw new EntityNotExistException("User", userId);
        film.removeLike(userId);
        log.info("Лайк был успешно удалён Film: {}, User: {}", filmId, userId);
        return film.getFilmLikesByUserId();
    }

    private void throwNotFoundIfFilmAbsentInStorage(Film film) {
        if (film.getId() == null || !filmStorage.isFilmInStorageById(film.getId())) {
            throw new NotFoundException("В хранилище отсутствует id: " + film.getId());
        }
        log.trace("Film прошёл проверку на отсутствие id в хранилище.");
    }

    private void validateRelease(Film film) {
        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(MOST_EARLY_RELEASE_DATE)) {
            throw new ValidationException("Дата релиза раньше 28 декабря 1895 г.");
        }
        log.trace("Film прошёл проверку на дату релиза <= 28.12.1895: {}.", film.getReleaseDate());
    }
}
