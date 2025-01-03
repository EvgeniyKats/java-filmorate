package ru.yandex.practicum.filmorate.storage.ratingmpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class RatingMpaDbStorage extends BaseRepository<RatingMpa> implements RatingMpaStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM rating_mpa;";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM rating_mpa WHERE rating_mpa_id = ?;";

    public RatingMpaDbStorage(JdbcTemplate jdbc, RowMapper<RatingMpa> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Optional<RatingMpa> getRatingMPAById(long ratingId) {
        Optional<RatingMpa> result = findOne(FIND_BY_ID_QUERY, ratingId);
        log.debug("getRatingMPAById, result = {}", result);
        return result;
    }

    @Override
    public List<RatingMpa> getAll() {
        List<RatingMpa> result = findMany(FIND_ALL_QUERY);
        log.debug("getAll, result = {}", result);
        return result;
    }
}
