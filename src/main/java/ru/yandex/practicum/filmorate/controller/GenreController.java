package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.genre.GenreDto;
import ru.yandex.practicum.filmorate.dto.genre.CreateGenreRequest;
import ru.yandex.practicum.filmorate.dto.genre.UpdateGenreRequest;
import ru.yandex.practicum.filmorate.service.genre.GenreService;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;

    @GetMapping
    public List<GenreDto> findAll() {
        log.info("Получен GET запрос /genres");
        return genreService.getAll();
    }

    @GetMapping("/{id}")
    public GenreDto getGenreById(@NotNull @PathVariable Integer id) {
        log.info("Получен GET запрос /genres/{}", id);
        return genreService.getGenreById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GenreDto createGenre(@RequestBody CreateGenreRequest request) {
        log.info("Получен POST запрос /genres");
        return genreService.createGenre(request);
    }

    @PutMapping("/{genreId}")
    public GenreDto updateGenre(@PathVariable("genreId") int genreId, @RequestBody UpdateGenreRequest request) {
        log.info("Получен PUT запрос /genres");
        return genreService.updateGenre(genreId, request);
    }

    @DeleteMapping("/{id}")
    public GenreDto deleteGenreById(@PathVariable Integer id) {
        log.info("Получен DELETE запрос /genres/{}", id);
        return genreService.deleteGenre(id);
    }
}
