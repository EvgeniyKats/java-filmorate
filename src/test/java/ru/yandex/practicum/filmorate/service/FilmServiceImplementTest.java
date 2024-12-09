package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.custom.EntityNotExistException;
import ru.yandex.practicum.filmorate.exception.custom.NotFoundException;
import ru.yandex.practicum.filmorate.exception.custom.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.film.FilmServiceImplement;
import ru.yandex.practicum.filmorate.service.user.UserServiceImplement;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmServiceImplementTest {
    private FilmService filmService;
    private UserService userService;

    @BeforeEach
    void beforeEach() {
        FilmStorage filmStorage = new InMemoryFilmStorage();
        UserStorage userStorage = new InMemoryUserStorage();
        userService = new UserServiceImplement(userStorage);
        filmService = new FilmServiceImplement(filmStorage, userStorage);
    }

    @Test
    void shouldSuccessCreateFilm() {
        Film film = Film.builder()
                .name("Name")
                .description("Description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(60)
                .build();
        Film add = filmService.createFilm(film);
        assertNotNull(add.getId());
        assertEquals(film, add);
        assertEquals(film.getName(), add.getName());
        assertEquals(film.getDescription(), add.getDescription());
        assertEquals(film.getReleaseDate(), add.getReleaseDate());
        assertEquals(film.getDuration(), add.getDuration());
    }

    @Test
    void shouldSuccessGetCreatedFilm() {
        assertEquals(0, filmService.findAll().size());

        Film film = Film.builder()
                .name("Name")
                .description("Description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(60)
                .build();
        filmService.createFilm(film);
        Film add = filmService.findAll().getFirst();
        assertNotNull(add.getId());
        assertEquals(film, add);
        assertEquals(film.getName(), add.getName());
        assertEquals(film.getDescription(), add.getDescription());
        assertEquals(film.getReleaseDate(), add.getReleaseDate());
        assertEquals(film.getDuration(), add.getDuration());
    }

    @Test
    void shouldSuccessCreate1000Films() {
        for (int i = 0; i < 1000; i++) {
            Film film = Film.builder()
                    .name("Name" + i)
                    .description("Description")
                    .releaseDate(LocalDate.of(2000, 1, 1))
                    .duration(60)
                    .build();
            filmService.createFilm(film);
            assertEquals(i + 1, filmService.findAll().size());
        }
    }

    @Test
    void shouldFailedCreateWithBadReleaseDate() {
        Film film = Film.builder()
                .name("Name")
                .description("Description")
                .releaseDate(FilmServiceImplement.MOST_EARLY_RELEASE_DATE.minusDays(1))
                .duration(60)
                .build();
        assertThrows(ValidationException.class, () -> filmService.createFilm(film));
    }

    @Test
    void shouldSuccessUpdateFilm() {
        Film film = Film.builder()
                .name("Name")
                .description("Description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(60)
                .build();
        filmService.createFilm(film);

        Film toUpdate = film.toBuilder()
                .name("Name2")
                .build();
        filmService.updateFilm(toUpdate);
        Film received = filmService.findAll().getFirst();
        assertNotNull(received.getId());
        assertEquals(toUpdate, received);
        assertEquals(toUpdate.getName(), received.getName());
        assertEquals(toUpdate.getDescription(), received.getDescription());
        assertEquals(toUpdate.getReleaseDate(), received.getReleaseDate());
        assertEquals(toUpdate.getDuration(), received.getDuration());
    }

    @Test
    void shouldSuccessUpdateFilm10TimesWithDifferenceNames() {
        Film film = Film.builder()
                .name("Name")
                .description("Description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(60)
                .build();
        filmService.createFilm(film);

        String name = "";
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) name = String.valueOf(i);
            Film finalFilm = film.toBuilder()
                    .name(name)
                    .build();
            assertDoesNotThrow(() -> filmService.updateFilm(finalFilm));
        }
    }

    @Test
    void shouldFailedUpdateIfFilmNotContainsInHolder() {
        Film film = Film.builder()
                .id(1L)
                .name("Name")
                .description("Description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(60)
                .build();
        assertThrows(NotFoundException.class, () -> filmService.updateFilm(film));
    }

    @Test
    void shouldFailedUpdateIfFilmNotHaveId() {
        Film film = Film.builder()
                .name("Name")
                .description("Description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(60)
                .build();
        assertThrows(NotFoundException.class, () -> filmService.updateFilm(film));
    }

    @Test
    void shouldBeZeroLikesIfNoAdd() {
        Film film = Film.builder()
                .name("Name")
                .description("Description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(60)
                .build();
        film = filmService.createFilm(film);
        assertEquals(0, film.getFilmLikesByUserId().size());
    }

    @Test
    void shouldAddOneLike() {
        Film film = Film.builder()
                .name("Name")
                .description("Description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(60)
                .build();
        film = filmService.createFilm(film);

        User user = User.builder()
                .id(1L)
                .email("test@test.ru")
                .name("Name")
                .login("Login")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        userService.createUser(user);

        List<Long> likes = filmService.addFilmLike(film.getId(), user.getId());
        assertEquals(1, likes.size());
        assertEquals(1, film.getFilmLikesByUserId().size());
    }

    @Test
    void shouldAddOnlyOneLikeByOneUser() {
        Film film = Film.builder()
                .name("Name")
                .description("Description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(60)
                .build();
        film = filmService.createFilm(film);

        User user = User.builder()
                .id(1L)
                .email("test@test.ru")
                .name("Name")
                .login("Login")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        userService.createUser(user);
        filmService.addFilmLike(film.getId(), user.getId());
        List<Long> likes = filmService.addFilmLike(film.getId(), user.getId());
        assertEquals(1, likes.size());
    }

    @Test
    void shouldBeAdd2LikesBy2Users() {
        Film film = Film.builder()
                .name("Name")
                .description("Description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(60)
                .build();
        film = filmService.createFilm(film);

        User user1 = User.builder()
                .id(1L)
                .email("test@test.ru")
                .name("Name")
                .login("Login")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        userService.createUser(user1);

        User user2 = User.builder()
                .id(2L)
                .email("test2@test.ru")
                .name("Name")
                .login("Login")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        userService.createUser(user2);

        filmService.addFilmLike(film.getId(), user1.getId());
        List<Long> likes = filmService.addFilmLike(film.getId(), user2.getId());
        assertEquals(2, likes.size());
    }

    @Test
    void shouldBeFailedAddLikeByUnknownUser() {
        Film film = Film.builder()
                .name("Name")
                .description("Description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(60)
                .build();
        film = filmService.createFilm(film);

        User user = User.builder()
                .id(1L)
                .email("test@test.ru")
                .name("Name")
                .login("Login")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        Film finalFilm = film;
        assertThrows(EntityNotExistException.class, () -> filmService.addFilmLike(finalFilm.getId(), user.getId()));
        assertEquals(0, film.getFilmLikesByUserId().size());
    }

    @Test
    void shouldBeFailedRemoveLikeByUnknownUser() {
        Film film = Film.builder()
                .name("Name")
                .description("Description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(60)
                .build();
        film = filmService.createFilm(film);

        User user = User.builder()
                .id(1L)
                .email("test@test.ru")
                .name("Name")
                .login("Login")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        Film finalFilm = film;
        assertThrows(EntityNotExistException.class, () -> filmService.removeFilmLike(finalFilm.getId(), user.getId()));
        assertEquals(0, film.getFilmLikesByUserId().size());
    }

    @Test
    void shouldSuccessRemoveOneLike() {
        Film film = Film.builder()
                .name("Name")
                .description("Description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(60)
                .build();
        film = filmService.createFilm(film);

        User user = User.builder()
                .id(1L)
                .email("test@test.ru")
                .name("Name")
                .login("Login")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        userService.createUser(user);

        filmService.addFilmLike(film.getId(), user.getId());
        List<Long> likes = filmService.removeFilmLike(film.getId(), user.getId());
        assertEquals(0, likes.size());
    }

    @Test
    void shouldBeFailedAddLikeIfNotAddFilm() {
        Film film = Film.builder()
                .id(1L)
                .name("Name")
                .description("Description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(60)
                .build();

        User user = User.builder()
                .id(1L)
                .email("test@test.ru")
                .name("Name")
                .login("Login")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        userService.createUser(user);

        assertThrows(EntityNotExistException.class, () -> filmService.addFilmLike(film.getId(), user.getId()));
        assertEquals(0, film.getFilmLikesByUserId().size());
    }

    @Test
    void shouldBeFailedRemoveLikeIfNotAddFilm() {
        Film film = Film.builder()
                .id(1L)
                .name("Name")
                .description("Description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(60)
                .build();

        User user = User.builder()
                .id(1L)
                .email("test@test.ru")
                .name("Name")
                .login("Login")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        userService.createUser(user);

        assertThrows(EntityNotExistException.class, () -> filmService.removeFilmLike(film.getId(), user.getId()));
        assertEquals(0, film.getFilmLikesByUserId().size());
    }

    @Test
    void shouldGetTop0Films() {
        assertEquals(0, filmService.getTopFilms(0).size());
        addSomeFilms(15);
        assertEquals(0, filmService.getTopFilms(0).size());
    }

    @Test
    void shouldGetTop1Films() {
        addSomeFilms(15);
        assertEquals(1, filmService.getTopFilms(1).size());
        assertEquals(15, filmService.getTopFilms(1).getFirst().getCountLikes());
    }

    @Test
    void shouldGetTop10Films() {
        addSomeFilms(10);
        assertEquals(10, filmService.getTopFilms(10).size());
        assertEquals(10, filmService.getTopFilms(10).getFirst().getCountLikes());
    }

    @Test
    void shouldGetTop10FilmsButRequestIs1000() {
        addSomeFilms(10);
        assertEquals(10, filmService.getTopFilms(1000).size());
        assertEquals(10, filmService.getTopFilms(1000).getFirst().getCountLikes());
    }

    private void addSomeFilms(int count) {
        for (int i = 0; i < count; i++) {
            Film film = Film.builder()
                    .name("Name" + i)
                    .description("Description")
                    .releaseDate(LocalDate.of(2000, 1, 1))
                    .duration(60)
                    .build();
            for (int j = 0; j < i + 1; j++) {
                film.addLike(j);
            }
            filmService.createFilm(film);
        }
    }
}