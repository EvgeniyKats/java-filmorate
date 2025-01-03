package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository("filmDbStorage")
public class FilmDbStorage extends BaseRepository<Film> implements FilmStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM film;";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM film WHERE film_id = ?;";
    private static final String INSERT_QUERY = "INSERT INTO film (name, description, release, duration, rating_mpa_id) " +
            "VALUES (?, ?, ?, ?, ?);";
    private static final String UPDATE_QUERY = "UPDATE film " +
            "SET name = ?, description = ?, release = ?, duration = ?, rating_mpa_id = ? WHERE film_id = ?;";
    private static final String DELETE_QUERY = "DELETE film WHERE film_id = ?;";
    private static final String GET_TOP_QUERY = """
            SELECT f.film_id,
                   f.name,
            	   f.description,
            	   f.release,
            	   f.duration,
            	   f.rating_mpa_id,
                   COUNT(fl.user_id) AS count_likes
            FROM film_like AS fl
            INNER JOIN film AS f ON fl.film_id = f.film_id
            GROUP BY f.film_id
            ORDER BY count_likes DESC
            LIMIT ?;
            """;

    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Optional<Film> getFilm(long id) {
        Optional<Film> result = findOne(FIND_BY_ID_QUERY, id);
        log.debug("getFilm result = {}", result);
        return result;
    }

    @Override
    public List<Film> getAllFilms() {
        List<Film> result = findMany(FIND_ALL_QUERY);
        log.debug("getAllFilms result = {}", result);
        return result;
    }

    @Override
    public Film addFilm(Film film) {
        long id = insert(INSERT_QUERY, Long.class,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId()
        );
        film.setId(id);
        log.debug("addFilm film = {}", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        update(UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );
        log.debug("updateFilm film = {}", film);
        return film;
    }

    @Override
    public void removeFilm(long id) {
        log.debug("removeFilm id = {}", id);
        delete(DELETE_QUERY, id);
    }

    @Override
    public List<Film> getTopFilms(int count) {
        log.debug("getTopFilms count = {}", count);
        return findMany(GET_TOP_QUERY, count);
    }
}
