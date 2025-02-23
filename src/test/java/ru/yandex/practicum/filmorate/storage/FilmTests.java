package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenrePair;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.friend.FriendDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.FilmGenresDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.like.FilmLikeDbStorage;
import ru.yandex.practicum.filmorate.storage.mapper.FilmGenresRowMapper;
import ru.yandex.practicum.filmorate.storage.mapper.FilmLikeRowMapper;
import ru.yandex.practicum.filmorate.storage.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.storage.mapper.FriendRowMapper;
import ru.yandex.practicum.filmorate.storage.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.storage.mapper.RatingMpaRowMapper;
import ru.yandex.practicum.filmorate.storage.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.storage.ratingmpa.RatingMpaDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Import({UserDbStorage.class,
        FriendDbStorage.class,
        FilmDbStorage.class,
        RatingMpaDbStorage.class,
        FilmLikeDbStorage.class,
        FilmGenresDbStorage.class,
        UserRowMapper.class,
        FriendRowMapper.class,
        FilmRowMapper.class,
        RatingMpaRowMapper.class,
        FilmGenresRowMapper.class,
        FilmLikeRowMapper.class,
        GenreDbStorage.class,
        GenreRowMapper.class
})

public class FilmTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;
    private final RatingMpaDbStorage ratingMpaDbStorage;
    private final FilmLikeDbStorage filmLikeStorage;
    private final FilmGenresDbStorage filmGenresStorage;
    private final GenreDbStorage genreDbStorage;

    @Test
    void shouldAddFilm() {
        assertThat(filmStorage.getFilm(1)).isEmpty();
        addFilm();
        assertThat(filmStorage.getFilm(1)).isPresent();
    }

    @Test
    void shouldGetBe2Films() {
        addFilm();
        addFilm();
        assertThat(filmStorage.getAllFilms()).size().isEqualTo(2);
    }

    @Test
    void shouldUpdateFilm() {
        Film f = addFilm();
        Film u = f.toBuilder().build();
        u.setName("new");
        long id = f.getId();
        assertThat(filmStorage.getFilm(id))
                .isPresent()
                .hasValueSatisfying(film -> assertThat(film).hasFieldOrPropertyWithValue("name", f.getName()));
        filmStorage.updateFilm(u);
        assertThat(filmStorage.getFilm(id))
                .isPresent()
                .hasValueSatisfying(film -> assertThat(film).hasFieldOrPropertyWithValue("name", u.getName()));
    }

    @Test
    void shouldDeleteFilm() {
        Film f = addFilm();
        filmStorage.removeFilm(f.getId());
        assertThat(filmStorage.getFilm(f.getId())).isEmpty();
    }

    @Test
    void shouldAddLike() {
        User u = addUser();
        Film f = addFilm();
        assertThat(filmLikeStorage.getFilmLikesByFilmId(f.getId())).size().isEqualTo(0);
        filmLikeStorage.addLike(f.getId(), u.getId());
        assertThat(filmLikeStorage.getFilmLikesByFilmId(f.getId())).size().isEqualTo(1);
    }

    @Test
    void shouldRemoveLike() {
        User u = addUser();
        Film f = addFilm();
        filmLikeStorage.addLike(f.getId(), u.getId());
        assertThat(filmLikeStorage.getFilmLikesByFilmId(f.getId())).size().isEqualTo(1);
        filmLikeStorage.removeLike(f.getId(), u.getId());
        assertThat(filmLikeStorage.getFilmLikesByFilmId(f.getId())).size().isEqualTo(0);
    }

    @Test
    void shouldGetLikePair() {
        User u = addUser();
        Film f = addFilm();
        filmLikeStorage.addLike(f.getId(), u.getId());
        assertThat(filmLikeStorage.getPairById(1))
                .isPresent()
                .hasValueSatisfying(pair -> assertThat(pair)
                        .hasFieldOrPropertyWithValue("id", 1L)
                        .hasFieldOrPropertyWithValue("filmId", f.getId())
                        .hasFieldOrPropertyWithValue("userId", u.getId()));
    }

    @Test
    void shouldGetLikedPairsOfUser() {
        User u = addUser();
        Film f1 = addFilm();
        Film f2 = addFilm();
        addFilm();
        filmLikeStorage.addLike(f1.getId(), u.getId());
        filmLikeStorage.addLike(f2.getId(), u.getId());
        assertThat(filmLikeStorage.getUserLikesByUserId(u.getId())).size().isEqualTo(2);
    }

    @Test
    void shouldGetMpaById() {
        assertThat(ratingMpaDbStorage.getRatingMPAById(1)).isPresent();
        assertThat(ratingMpaDbStorage.getRatingMPAById(100)).isEmpty();
    }

    @Test
    void shouldGetAllMpa() {
        assertThat(ratingMpaDbStorage.getAll()).size().isGreaterThanOrEqualTo(3);
    }

    @Test
    void shouldGetGenreById() {
        assertThat(genreDbStorage.getGenre(1)).isPresent();
        assertThat(genreDbStorage.getGenre(100)).isEmpty();
    }

    @Test
    void shouldGetAllGenres() {
        assertThat(genreDbStorage.getAllGenres()).size().isGreaterThanOrEqualTo(3);
    }

    @Test
    void shouldGetPairsOfFilmAndGenre() {
        assertThat(filmGenresStorage.getPairsOfFilmById(1)).isEmpty();
        Film f = addFilm();
        filmGenresStorage.addGenreToFilm(f.getId(), f.getGenres().stream().iterator().next().getId());
        assertThat(filmGenresStorage.getPairById(1)).isPresent();
        assertThat(filmGenresStorage.getPairsOfFilmById(1)).isNotEmpty();
    }

    @Test
    void shouldGetPairsOfListFilmId() {
        Film f1 = addFilm();
        Film f2 = addFilm();
        Film f3 = addFilm();
        filmGenresStorage.addGenreToFilm(f1.getId(), f1.getGenres().iterator().next().getId());
        filmGenresStorage.addGenreToFilm(f2.getId(), f2.getGenres().iterator().next().getId());
        filmGenresStorage.addGenreToFilm(f3.getId(), f3.getGenres().iterator().next().getId());
        List<Film> films = List.of(f1, f2);
        List<FilmGenrePair> pairs = filmGenresStorage.getPairsByListOfFilmId(films.stream().map(Film::getId).toList());
        assertThat(pairs).size().isEqualTo(2);
    }

    @Test
    void shouldAddGenreToFilm() {
        Film f = addFilm();
        filmGenresStorage.addGenreToFilm(f.getId(), f.getGenres().stream().iterator().next().getId());
        Genre genre = genreDbStorage.getGenre(2).orElseThrow(RuntimeException::new);
        f.addGenre(genre);
        filmStorage.updateFilm(f);
        filmGenresStorage.addGenreToFilm(f.getId(), genre.getId());
        assertThat(filmGenresStorage.getPairsOfFilmById(1)).size().isEqualTo(2);
    }

    @Test
    void shouldRemoveGenreFromFilm() {
        Film f = addFilm();
        int genreId = f.getGenres().iterator().next().getId();

        filmGenresStorage.addGenreToFilm(f.getId(), genreId);
        assertThat(filmGenresStorage.getPairsOfFilmById(1)).size().isEqualTo(1);

        filmGenresStorage.removeGenreOfFilm(f.getId(), genreId);
        assertThat(filmGenresStorage.getPairsOfFilmById(1)).size().isEqualTo(0);
    }

    @Test
    void shouldGetTopFilms() {
        assertThat(filmStorage.getTopFilms(0)).size().isEqualTo(0);
        Film film3Likes = addFilm();
        Film film2Likes = addFilm();
        Film film1Likes = addFilm();
        User u1 = addUser();
        User u2 = addUser();
        User u3 = addUser();

        addLikes(film3Likes, List.of(u1, u2, u3));
        addLikes(film2Likes, List.of(u1, u2));
        addLikes(film1Likes, List.of(u1));

        assertThat(filmStorage.getTopFilms(0)).size().isEqualTo(0);
        assertThat(filmStorage.getTopFilms(1)).size().isEqualTo(1);
        assertThat(filmStorage.getTopFilms(3)).size().isEqualTo(3);
        assertThat(filmStorage.getTopFilms(4)).size().isEqualTo(3);
        assertThat(filmStorage.getTopFilms(1000)).size().isEqualTo(3);
    }

    private User addUser() {
        long id = getIdNextUser();
        return userStorage.addUser(User.builder()
                .email("test" + id + "@test.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.now().minusDays(1))
                .build());
    }

    private long getIdNextUser() {
        long id = 1;
        while (userStorage.getUserById(id).isPresent()) {
            id++;
        }
        return id;
    }

    private Film addFilm() {
        RatingMpa ratingMpa = ratingMpaDbStorage.getRatingMPAById(1).orElseThrow(RuntimeException::new);
        Genre genre = genreDbStorage.getGenre(1).orElseThrow(RuntimeException::new);
        Film res = filmStorage.addFilm(Film.builder()
                .name("name")
                .duration(1)
                .releaseDate(LocalDate.now().minusDays(1))
                .description("descr")
                .mpaId(ratingMpa.getId())
                .build());
        res.addGenre(genre);
        return res;
    }

    private void addLikes(Film f, List<User> users) {
        users.forEach(user -> filmLikeStorage.addLike(f.getId(), user.getId()));
    }
}
