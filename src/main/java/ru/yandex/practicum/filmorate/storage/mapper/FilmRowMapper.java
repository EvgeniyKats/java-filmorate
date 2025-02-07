package ru.yandex.practicum.filmorate.storage.mapper;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.storage.ratingmpa.RatingMpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
@AllArgsConstructor
public class FilmRowMapper implements RowMapper<Film> {
    private final RatingMpaStorage ratingMPAStorage;

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong("film_id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate release = rs.getDate("release").toLocalDate();
        int duration = rs.getInt("duration");
        int ratingMpaId = rs.getInt("rating_mpa_id");
        RatingMpa ratingMpa = ratingMPAStorage.getRatingMPAById(ratingMpaId).orElse(null);

        return Film.builder()
                .id(id)
                .name(name)
                .description(description)
                .releaseDate(release)
                .duration(duration)
                .mpa(ratingMpa)
                .build();
    }
}
