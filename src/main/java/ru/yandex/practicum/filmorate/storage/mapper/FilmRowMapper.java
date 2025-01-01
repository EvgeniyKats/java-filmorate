package ru.yandex.practicum.filmorate.storage.mapper;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.storage.genre.FilmGenresStorage;
import ru.yandex.practicum.filmorate.storage.like.FilmLikesStorage;
import ru.yandex.practicum.filmorate.storage.ratingmpa.RatingMpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Component
@AllArgsConstructor
public class FilmRowMapper implements RowMapper<Film> {
    private final RatingMpaStorage ratingMPAStorage;
    private final FilmGenresStorage filmGenresStorage;
    private final FilmLikesStorage filmLikesStorage;

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong("film_id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate release = convertDateToLocalDate(rs.getDate("release"));
        int duration = rs.getInt("duration");
        int ratingMpa = rs.getInt("rating_id");
        RatingMpa ratingMPA = ratingMPAStorage.getRatingMPAById(ratingMpa).orElse(null);

        Film result = Film.builder()
                .id(id)
                .name(name)
                .description(description)
                .releaseDate(release)
                .duration(duration)
                .ratingMpa(ratingMPA)
                .build();
        fillFilmGenres(result);
        fillFilmLikes(result);

        return result;
    }

    private LocalDate convertDateToLocalDate(Date date) {
        return LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    private void fillFilmGenres(Film film) {
        filmGenresStorage.getGenresByFilmId(film.getId()).forEach(pair -> film.addGenre(pair.getGenreId()));
    }

    private void fillFilmLikes(Film film) {
        filmLikesStorage.getFilmLikesByFilmId(film.getId())
                .forEach(filmLikePair -> film.addLike(filmLikePair.getUserId()));
    }
}
