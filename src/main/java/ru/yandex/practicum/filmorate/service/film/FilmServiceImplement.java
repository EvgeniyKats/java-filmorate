package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.film.CreateFilmDto;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmDto;
import ru.yandex.practicum.filmorate.dto.genre.GenreDto;
import ru.yandex.practicum.filmorate.dto.ratingmpa.RatingMpaDto;
import ru.yandex.practicum.filmorate.exception.custom.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.custom.NotFoundException;
import ru.yandex.practicum.filmorate.exception.custom.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenrePair;
import ru.yandex.practicum.filmorate.model.FilmLikePair;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.FilmGenresStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.like.FilmLikeStorage;
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
    private final FilmLikeStorage filmLikeStorage;
    private final FilmGenresStorage filmGenresStorage;
    private final GenreStorage genreStorage;
    private final RatingMpaStorage ratingMpaStorage;

    public FilmServiceImplement(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                                @Qualifier("userDbStorage") UserStorage userStorage,
                                FilmLikeStorage filmLikeStorage, FilmGenresStorage filmGenresStorage, GenreStorage genreStorage, RatingMpaStorage ratingMpaStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.filmLikeStorage = filmLikeStorage;
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
        log.info("findAll success");
        return result;
    }

    @Override
    public FilmDto createFilm(CreateFilmDto request) {
        validateRelease(request.getReleaseDate());
        validateGenres(request.getGenresDto());
        validateMpa(request.getMpaDto());
        log.trace("Успешная валидация, createFilm");
        Film film = FilmMapper.mapToFilm(request);
        log.debug("Преобразование в фильм, createFilm, {}", film);
        filmStorage.addFilm(film);
        film.getGenres().forEach(genre -> filmGenresStorage.addGenreToFilm(film.getId(), genre.getId()));
        log.info("Film {}, был добавлен в хранилище.", film);
        return FilmMapper.mapToFilmDto(film);
    }

    @Override
    public FilmDto updateFilm(UpdateFilmDto request) {
        Film film = throwNotFoundIfIdAbsentInStorage(request.getId());
        validateRelease(request.getReleaseDate());
        validateMpa(request.getMpaDto());
        validateGenres(request.getGenresDto());
        log.trace("Успешная валидация, updateFilm");
        film.updateFieldsFromUpdateDto(request);
        log.trace("Обновленный фильм: {}", film);
        if (request.hasGenres()) {
            List<FilmGenrePair> pairs = filmGenresStorage.getGenresByFilmId(film.getId());
            pairs.forEach(p -> filmGenresStorage.removeGenreOfFilm(p.getFilmId(), p.getGenreId()));
            film.getGenres().forEach(genre -> filmGenresStorage.addGenreToFilm(request.getId(), genre.getId()));
        }
        film = filmStorage.updateFilm(film);
        log.info("updateFilm success");
        return FilmMapper.mapToFilmDto(film);
    }

    @Override
    public FilmDto getFilmById(long id) {
        Film film = throwNotFoundIfIdAbsentInStorage(id);
        log.trace("Фильм прошел проверку на null.");
        log.info("getFilmById success");
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
        throwNotFoundIfIdAbsentInStorage(filmId);
        userStorage.getUserById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с id=" + userId + " отсутствует в хранилище."));
        log.trace("addFilmLike, пройдены проверки");
        filmLikeStorage.addLike(filmId, userId);
        log.info("Лайк был успешно поставлен Film: {}, User: {}", filmId, userId);
        return filmLikeStorage.getFilmLikesByFilmId(filmId).stream()
                .map(FilmLikePair::getUserId)
                .toList();
    }

    @Override
    public List<Long> removeFilmLike(Long filmId, Long userId) {
        throwNotFoundIfIdAbsentInStorage(filmId);
        userStorage.getUserById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с id=" + userId + " отсутствует в хранилище."));
        log.trace("removeFilmLike, пройдены проверки");
        filmLikeStorage.removeLike(filmId, userId);
        log.info("Лайк был успешно удалён Film: {}, User: {}", filmId, userId);
        return filmLikeStorage.getFilmLikesByFilmId(filmId).stream()
                .map(FilmLikePair::getUserId)
                .toList();
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

    private void validateGenres(List<GenreDto> genresDto) {
        if (genresDto == null) return;
        genresDto.forEach(g -> {
            Genre gData = genreStorage.getGenre(g.getId()).orElseThrow(() ->
                    new IncorrectParameterException("genre id = " + g.getId(),
                            "Жанр с таким id отсутствует в хранилище"));
            g.setName(gData.getName());
        });
    }

    private void validateMpa(RatingMpaDto dto) {
        if (dto == null) return;
        RatingMpa m = ratingMpaStorage.getRatingMPAById(dto.getId()).orElseThrow(() ->
                new IncorrectParameterException("mpa id = " + dto.getId(),
                        "Рейтинг с таким id отсутствует в хранилище"));
        dto.setName(m.getName());
    }
}
