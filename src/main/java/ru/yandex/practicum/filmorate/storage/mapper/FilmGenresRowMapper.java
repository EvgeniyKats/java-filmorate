package ru.yandex.practicum.filmorate.storage.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmGenrePair;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilmGenresRowMapper implements RowMapper<FilmGenrePair> {
    @Override
    public FilmGenrePair mapRow(ResultSet rs, int rowNum) throws SQLException {
        FilmGenrePair result = new FilmGenrePair();
        result.setId(rs.getLong("id"));
        result.setFilmId(rs.getLong("film_id"));
        result.setGenreId(rs.getInt("genre_id"));
        return result;
    }
}
