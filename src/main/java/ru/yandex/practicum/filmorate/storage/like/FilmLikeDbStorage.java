package ru.yandex.practicum.filmorate.storage.like;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmLikePair;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.util.List;
import java.util.Optional;

@Component
public class FilmLikeDbStorage extends BaseRepository<FilmLikePair> implements FilmLikesStorage {
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
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public List<FilmLikePair> getFilmLikesByFilmId(long filmId) {
        return findMany(FIND_BY_FILM_ID_QUERY, filmId);
    }

    @Override
    public List<FilmLikePair> getUserLikesByUserId(long userId) {
        return findMany(FIND_BY_USER_ID_QUERY, userId);
    }

    @Override
    public void addLike(long filmId, long userId) {
        insert(INSERT_QUERY, Long.class, filmId, userId);
    }

    @Override
    public void removeLike(long filmId, long userId) {
        delete(DELETE_BY_FILM_AND_USER_QUERY, filmId, userId);
    }
}
