package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.film.CreateFilmDto;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmDto;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public List<FilmDto> findAll() {
        log.info("Получен GET запрос /films");
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public FilmDto getFilmById(@PathVariable Long id) {
        log.info("Получен GET запрос /films/{}", id);
        return filmService.getFilmById(id);
    }

    @GetMapping("/popular")
    public List<FilmDto> getTopFilms(
            @Min(0) @RequestParam(defaultValue = "10") Integer count) {
        log.info("Получен GET запрос /films/popular?count={}", count);
        return filmService.getTopFilms(count);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FilmDto createFilm(@Validated @RequestBody CreateFilmDto request) {
        log.info("Получен POST запрос /films");
        System.out.println(request);
        return filmService.createFilm(request);
    }

    @PutMapping
    public FilmDto updateFilm(@Validated @RequestBody UpdateFilmDto request) {
        log.info("Получен PUT запрос /films");
        return filmService.updateFilm(request);
    }

    @PutMapping("/{id}/like/{userId}")
    public List<Long> addLike(@PathVariable Long id,
                              @PathVariable Long userId) {
        log.info("Получен PUT запрос /films/{}/like/{}", id, userId);
        return filmService.addFilmLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public List<Long> removeLike(@PathVariable Long id,
                                 @PathVariable Long userId) {
        log.info("Получен DELETE запрос /films/{}/like/{}", id, userId);
        return filmService.removeFilmLike(id, userId);
    }
}
