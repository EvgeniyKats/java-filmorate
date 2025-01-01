package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class GenreDbStorage extends BaseRepository<Genre> implements GenreStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM genre;";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM genre WHERE genre_id = ?;";
    private static final String INSERT_QUERY = "INSERT INTO genre (name) VALUES (?)";
    private static final String UPDATE_QUERY = "UPDATE genre SET name = ? WHERE genre_id = ?";
    private static final String DELETE_QUERY = "DELETE genre WHERE genre_id = ?";

    public GenreDbStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Optional<Genre> getGenre(int id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public List<Genre> getAllGenres() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Genre addGenre(Genre genre) {
        int id = insert(INSERT_QUERY, Integer.class, genre.getName());
        genre.setId(id);
        return genre;
    }

    @Override
    public Genre updateGenre(Genre genre) {
        update(UPDATE_QUERY,
                genre.getName(),
                genre.getId());
        return genre;
    }

    @Override
    public boolean removeGenre(int id) {
        return delete(DELETE_QUERY, id);
    }
}
