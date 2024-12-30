package ru.yandex.practicum.filmorate.storage.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmLikePair;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilmLikeRowMapper implements RowMapper<FilmLikePair> {
    @Override
    public FilmLikePair mapRow(ResultSet rs, int rowNum) throws SQLException {
        FilmLikePair result = new FilmLikePair();
        result.setId(rs.getLong("id"));
        result.setFilmId(rs.getLong("film_id"));
        result.setUserId(rs.getLong("user_id"));
        return result;
    }
}
