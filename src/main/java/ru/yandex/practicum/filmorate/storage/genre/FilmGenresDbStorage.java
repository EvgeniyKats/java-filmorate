package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmGenrePair;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class FilmGenresDbStorage extends BaseRepository<FilmGenrePair> implements FilmGenresStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM film_genre;";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM film_genre WHERE id = ?;";
    private static final String FIND_MULTIPLE_BY_ID_TEMPLATE = "SELECT * FROM film_genre WHERE film_id IN (%s);";
    private static final String FIND_BY_FILM_ID_QUERY = "SELECT * FROM film_genre WHERE film_id = ?;";
    private static final String INSERT_QUERY = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?);";
    private static final String DELETE_QUERY = "DELETE FROM film_genre WHERE film_id = ? AND genre_id = ?;";

    public FilmGenresDbStorage(JdbcTemplate jdbc, RowMapper<FilmGenrePair> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Optional<FilmGenrePair> getPairById(long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public List<FilmGenrePair> getAllPairs() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public List<FilmGenrePair> getPairsOfFilmById(long filmId) {
        return findMany(FIND_BY_FILM_ID_QUERY, filmId);
    }

    @Override
    public List<FilmGenrePair> getPairsByListOfFilmId(List<Long> filmsIds) {
        String inSql = String.join(",", Collections.nCopies(filmsIds.size(), "?"));
        final String findMultipleByIdQuery = String.format(FIND_MULTIPLE_BY_ID_TEMPLATE, inSql);
        return findMany(findMultipleByIdQuery, filmsIds.toArray());
    }

    @Override
    public void addGenreToFilm(long filmId, int genreId) {
        insert(INSERT_QUERY, Long.class, filmId, genreId);
    }

    @Override
    public void removeGenreOfFilm(long filmId, int genreId) {
        delete(DELETE_QUERY, filmId, genreId);
    }
}
