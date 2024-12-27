package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.util.List;

@Repository
public class GenreDbStorage extends BaseRepository<Genre> implements GenreStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM genre";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM genre WHERE id = ?";
    private static final String INSERT_QUERY = "INSERT INTO genre(name) VALUES (?) returning id";
    private static final String UPDATE_QUERY = "UPDATE genre SET name = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE genre WHERE id = ?";

    public GenreDbStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Genre getGenre(long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public List<Genre> getAllGenres() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public void addGenre(Genre genre) {
        insert(INSERT_QUERY,
                genre.getName());
    }

    @Override
    public Genre updateGenre(Genre genre) {
        update(UPDATE_QUERY,
                genre.getId(),
                genre.getName());
        return genre;
    }

    @Override
    public void removeGenre(long id) {
        delete(DELETE_QUERY, id);
    }

    @Override
    public boolean isGenreInStorageById(long id) {
        return findOne(FIND_BY_ID_QUERY, id) != null;
    }

    @Override
    public boolean isGenreNotExistInStorageById(long id) {
        return findOne(FIND_BY_ID_QUERY, id) == null;
    }
}
