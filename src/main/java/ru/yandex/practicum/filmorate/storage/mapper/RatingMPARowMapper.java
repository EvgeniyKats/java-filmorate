package ru.yandex.practicum.filmorate.storage.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.RatingMPA;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class RatingMPARowMapper implements RowMapper<RatingMPA> {
    @Override
    public RatingMPA mapRow(ResultSet rs, int rowNum) throws SQLException {
        RatingMPA result = new RatingMPA();
        result.setId(rs.getInt("rating_id"));
        result.setName(rs.getString("name"));
        return result;
    }
}
