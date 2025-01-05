package ru.yandex.practicum.filmorate.service.genre;

import ru.yandex.practicum.filmorate.dto.genre.CreateGenreDto;
import ru.yandex.practicum.filmorate.dto.genre.GenreDto;
import ru.yandex.practicum.filmorate.dto.genre.UpdateGenreDto;

import java.util.List;

public interface GenreService {
    List<GenreDto> getAll();

    GenreDto getGenreById(int id);

    GenreDto createGenre(CreateGenreDto request);

    GenreDto updateGenre(int id, UpdateGenreDto request);

    GenreDto deleteGenre(int id);
}
