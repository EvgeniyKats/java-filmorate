package ru.yandex.practicum.filmorate.storage.ratingmpa;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.util.List;
import java.util.Optional;

@Component
public class RatingMpaDbStorage extends BaseRepository<RatingMpa> implements RatingMpaStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM rating_mpa;";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM rating_mpa WHERE rating_mpa_id = ?;";

    public RatingMpaDbStorage(JdbcTemplate jdbc, RowMapper<RatingMpa> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Optional<RatingMpa> getRatingMPAById(long ratingId) {
        return findOne(FIND_BY_ID_QUERY, ratingId);
    }

    @Override
    public List<RatingMpa> getAll() {
        return findMany(FIND_ALL_QUERY);
    }
}
