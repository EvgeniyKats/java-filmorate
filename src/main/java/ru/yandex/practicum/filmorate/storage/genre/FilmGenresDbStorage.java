package ru.yandex.practicum.filmorate.storage.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmGenrePair;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class FilmGenresDbStorage extends BaseRepository<FilmGenrePair> implements FilmGenresStorage {
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM film_genre WHERE id = ?;";
    private static final String FIND_BY_FILM_ID_QUERY = "SELECT * FROM film_genre WHERE film_id = ?;";
    private static final String INSERT_QUERY = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?);";
    private static final String DELETE_QUERY = "DELETE FROM film_genre WHERE film_id = ? AND genre_id = ?;";

    public FilmGenresDbStorage(JdbcTemplate jdbc, RowMapper<FilmGenrePair> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Optional<FilmGenrePair> getPairById(long id) {
        Optional<FilmGenrePair> result = findOne(FIND_BY_ID_QUERY, id);
        log.debug("getPairById, result = {}", result);
        return result;
    }

    @Override
    public List<FilmGenrePair> getGenresByFilmId(long filmId) {
        List<FilmGenrePair> result = findMany(FIND_BY_FILM_ID_QUERY, filmId);
        log.debug("getGenresByFilmId, result = {}", result);
        return result;
    }

    @Override
    public void addGenreToFilm(long filmId, int genreId) {
        log.debug("addGenreToFilm, filmId = {}, genreId = {}", filmId, genreId);
        insert(INSERT_QUERY, Long.class, filmId, genreId);
    }

    @Override
    public void removeGenreOfFilm(long filmId, int genreId) {
        log.debug("removeGenreOfFilm, filmId = {}, genreId = {}", filmId, genreId);
        delete(DELETE_QUERY, filmId, genreId);
    }
}
