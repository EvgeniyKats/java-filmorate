package ru.yandex.practicum.filmorate.storage.like;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmLikePair;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class FilmLikeDbStorage extends BaseRepository<FilmLikePair> implements FilmLikeStorage {
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM film_like WHERE id = ?;";
    private static final String FIND_BY_FILM_ID_QUERY = "SELECT * FROM film_like WHERE film_id = ?;";
    private static final String FIND_BY_USER_ID_QUERY = "SELECT * FROM film_like WHERE user_id = ?;";
    private static final String INSERT_QUERY = "INSERT INTO film_like (film_id, user_id) VALUES (?, ?);";
    private static final String DELETE_BY_FILM_AND_USER_QUERY = "DELETE FROM film_like WHERE " +
            "film_id = ? AND user_id = ?;";

    public FilmLikeDbStorage(JdbcTemplate jdbc, RowMapper<FilmLikePair> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Optional<FilmLikePair> getPairById(long id) {
        Optional<FilmLikePair> result = findOne(FIND_BY_ID_QUERY, id);
        log.debug("getPairById, result = {}", result);
        return result;
    }

    @Override
    public List<FilmLikePair> getFilmLikesByFilmId(long filmId) {
        List<FilmLikePair> result = findMany(FIND_BY_FILM_ID_QUERY, filmId);
        log.debug("getFilmLikesByFilmId, result = {}", result);
        return result;
    }

    @Override
    public List<FilmLikePair> getUserLikesByUserId(long userId) {
        List<FilmLikePair> result = findMany(FIND_BY_USER_ID_QUERY, userId);
        log.debug("getUserLikesByUserId, result = {}", result);
        return result;
    }

    @Override
    public void addLike(long filmId, long userId) {
        log.debug("addLike, filmId = {}, userId = {}", filmId, userId);
        insert(INSERT_QUERY, Long.class, filmId, userId);
    }

    @Override
    public void removeLike(long filmId, long userId) {
        log.debug("removeLike, filmId = {}, userId = {}", filmId, userId);
        delete(DELETE_BY_FILM_AND_USER_QUERY, filmId, userId);
    }
}
