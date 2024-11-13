package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.DuplicateException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    private FilmController filmController;

    @BeforeEach
    void beforeEach() {
        filmController = new FilmController();
    }

    @Test
    void shouldSuccessCreateFilm() {
        Film film = Film.builder()
                .name("Name")
                .description("Description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(60)
                .build();
        Film add = filmController.createFilm(film);
        assertNotNull(add.getId());
        assertEquals(film, add);
        assertEquals(film.getName(), add.getName());
        assertEquals(film.getDescription(), add.getDescription());
        assertEquals(film.getReleaseDate(), add.getReleaseDate());
        assertEquals(film.getDuration(), add.getDuration());
    }

    @Test
    void shouldSuccessGetCreatedFilm() {
        assertEquals(0, filmController.findAll().size());

        Film film = Film.builder()
                .name("Name")
                .description("Description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(60)
                .build();
        filmController.createFilm(film);
        Film add = filmController.findAll().iterator().next();
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
            filmController.createFilm(film);
            assertEquals(i + 1, filmController.findAll().size());
        }
    }

    @Test
    void shouldThrowsDuplicateExceptionIfCreateSameFilm() {
        Film film = Film.builder()
                .name("Name")
                .description("Description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(60)
                .build();
        filmController.createFilm(film);
        assertEquals(1, filmController.findAll().size());
        assertThrows(DuplicateException.class, () -> filmController.createFilm(film));
    }

    @Test
    void shouldFailedCreateWithBadReleaseDate() {
        Film film = Film.builder()
                .name("Name")
                .description("Description")
                .releaseDate(FilmController.MOST_EARLY_RELEASE_DATE.minusDays(1))
                .duration(60)
                .build();
        assertThrows(ValidationException.class, () -> filmController.createFilm(film));
    }

    @Test
    void shouldSuccessUpdateFilm() {
        Film film = Film.builder()
                .name("Name")
                .description("Description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(60)
                .build();
        filmController.createFilm(film);

        Film toUpdate = film.toBuilder()
                .name("Name2")
                .build();
        filmController.updateFilm(toUpdate);
        Film received = filmController.findAll().iterator().next();
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
        filmController.createFilm(film);

        String name = "";
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) name = String.valueOf(i);
            Film finalFilm = film.toBuilder()
                    .name(name)
                    .build();
            assertDoesNotThrow(() -> filmController.updateFilm(finalFilm));
        }
    }

    @Test
    void shouldFailedUpdateWithRepeatNameByOtherFilm() {
        Film film = Film.builder()
                .name("Name")
                .description("Description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(60)
                .build();
        filmController.createFilm(film);

        film = film.toBuilder()
                .name("Name2")
                .build();
        filmController.createFilm(film);
        assertEquals(2, filmController.findAll().size());

        Film another = film.toBuilder()
                .name("Name")
                .build();
        assertThrows(DuplicateException.class, () -> filmController.updateFilm(another));
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
        assertThrows(ValidationException.class, () -> filmController.updateFilm(film));
    }

    @Test
    void shouldFailedUpdateIfFilmNotHaveId() {
        Film film = Film.builder()
                .name("Name")
                .description("Description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(60)
                .build();
        assertThrows(ValidationException.class, () -> filmController.updateFilm(film));
    }
}