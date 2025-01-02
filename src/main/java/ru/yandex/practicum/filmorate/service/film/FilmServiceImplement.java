package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.film.CreateFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.exception.custom.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.custom.NotFoundException;
import ru.yandex.practicum.filmorate.exception.custom.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenrePair;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.FilmGenresStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.like.FilmLikesStorage;
import ru.yandex.practicum.filmorate.storage.ratingmpa.RatingMpaStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class FilmServiceImplement implements FilmService {
    public static final LocalDate MOST_EARLY_RELEASE_DATE =
            LocalDate.of(1895, 12, 28);

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final FilmLikesStorage filmLikesStorage;
    private final FilmGenresStorage filmGenresStorage;
    private final GenreStorage genreStorage;
    private final RatingMpaStorage ratingMpaStorage;

    public FilmServiceImplement(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                                @Qualifier("userDbStorage") UserStorage userStorage,
                                FilmLikesStorage filmLikesStorage, FilmGenresStorage filmGenresStorage, GenreStorage genreStorage, RatingMpaStorage ratingMpaStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.filmLikesStorage = filmLikesStorage;
        this.filmGenresStorage = filmGenresStorage;
        this.genreStorage = genreStorage;
        this.ratingMpaStorage = ratingMpaStorage;
    }

    @Override
    public List<FilmDto> findAll() {
        List<FilmDto> result = filmStorage.getAllFilms().stream()
                .map(FilmMapper::mapToFilmDto)
                .toList();
        log.debug("Текущий список фильмов: {}.", result);
        return result;
    }

    @Override
    public FilmDto createFilm(CreateFilmRequest request) {
        validateRelease(request.getReleaseDate());
        validateGenres(request.getGenres());
        validateMpa(request.getMpa());
        Film film = FilmMapper.mapToFilm(request);
        filmStorage.addFilm(film);
        film.getGenres().forEach(genre -> filmGenresStorage.addFilmGenre(film.getId(), genre.getId()));
        log.info("Film {}, был добавлен в хранилище.", film);
        return FilmMapper.mapToFilmDto(film);
    }

    @Override
    public FilmDto updateFilm(UpdateFilmRequest request) {
        Film film = throwNotFoundIfIdAbsentInStorage(request.getId());
        validateRelease(request.getReleaseDate());
        validateMpa(request.getMpa());
        FilmMapper.updateFilmFields(film, request);
        if (request.hasGenres()) {
            validateGenres(request.getGenres());
            List<FilmGenrePair> pairs = filmGenresStorage.getGenresByFilmId(film.getId());
            pairs.forEach(p -> filmGenresStorage.removeFilmGenre(p.getFilmId(), p.getGenreId()));
            film.getGenres().forEach(genre -> filmGenresStorage.addFilmGenre(request.getId(), genre.getId()));
        }
        film = filmStorage.updateFilm(film);
        return FilmMapper.mapToFilmDto(film);
    }

    @Override
    public FilmDto getFilmById(long id) {
        Film film = throwNotFoundIfIdAbsentInStorage(id);
        log.trace("Фильм прошел проверку на null.");
        return FilmMapper.mapToFilmDto(film);
    }

    @Override
    public List<FilmDto> getTopFilms(Integer count) {
        log.trace("Параметр count прошёл проверки на корректность.");
        return filmStorage.getTopFilms(count).stream()
                .map(FilmMapper::mapToFilmDto)
                .toList();
    }

    @Override
    public List<Long> addFilmLike(Long filmId, Long userId) {
        Film film = throwNotFoundIfIdAbsentInStorage(filmId);
        userStorage.getUserById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с id=" + userId + " отсутствует в хранилище."));
        film.addLike(userId);
        filmLikesStorage.addLike(filmId, userId);
        log.info("Лайк был успешно поставлен Film: {}, User: {}", filmId, userId);
        return film.getFilmLikesByUserId();
    }

    @Override
    public List<Long> removeFilmLike(Long filmId, Long userId) {
        Film film = throwNotFoundIfIdAbsentInStorage(filmId);
        userStorage.getUserById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с id=" + userId + " отсутствует в хранилище."));
        film.removeLike(userId);
        filmLikesStorage.removeLike(filmId, userId);
        log.info("Лайк был успешно удалён Film: {}, User: {}", filmId, userId);
        return film.getFilmLikesByUserId();
    }

    private Film throwNotFoundIfIdAbsentInStorage(long id) {
        Film film = filmStorage.getFilm(id).orElseThrow(
                () -> new NotFoundException("В хранилище отсутствует id: " + id));
        log.trace("Film прошёл проверку на отсутствие id в хранилище.");
        return film;
    }

    private void validateRelease(LocalDate release) {
        if (release != null && release.isBefore(MOST_EARLY_RELEASE_DATE)) {
            throw new ValidationException("Дата релиза раньше 28 декабря 1895 г.");
        }
        log.trace("Film прошёл проверку на дату релиза <= 28.12.1895: {}.", release);
    }

    private void validateGenres(List<Genre> genres) {
        if (genres == null) return;
        genres.forEach(g -> {
            Genre gData = genreStorage.getGenre(g.getId()).orElseThrow(() ->
                    new IncorrectParameterException("genre id = " + g.getId(),
                            "Жанр с таким id отсутствует в хранилище"));
            g.setName(gData.getName());
        });
    }

    private void validateMpa(RatingMpa ratingMpa) {
        if (ratingMpa == null) return;
        RatingMpa m = ratingMpaStorage.getRatingMPAById(ratingMpa.getId()).orElseThrow(() ->
                new IncorrectParameterException("mpa id = " + ratingMpa.getId(),
                        "Рейтинг с таким id отсутствует в хранилище"));
        ratingMpa.setName(m.getName());
    }
}
