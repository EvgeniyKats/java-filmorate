package ru.yandex.practicum.filmorate.storage.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class FilmRowMapper implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong("film_id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate release = rs.getDate("release").toLocalDate();
        int duration = rs.getInt("duration");
        int ratingMpaId = rs.getInt("rating_mpa_id");

        return Film.builder()
                .id(id)
                .name(name)
                .description(description)
                .releaseDate(release)
                .duration(duration)
                .mpaId(ratingMpaId)
                .build();
    }
}
