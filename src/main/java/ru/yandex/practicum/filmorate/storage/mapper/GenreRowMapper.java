package ru.yandex.practicum.filmorate.storage.mapper;


import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class GenreRowMapper implements RowMapper<Genre> {
    @Override
    public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
        Genre result = new Genre();
        result.setId(rs.getInt("genre_id"));
        result.setName(rs.getString("name"));
        return result;
    }
}
