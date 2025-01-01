package ru.yandex.practicum.filmorate.service.genre;

import ru.yandex.practicum.filmorate.dto.genre.CreateGenreRequest;
import ru.yandex.practicum.filmorate.dto.genre.GenreDto;
import ru.yandex.practicum.filmorate.dto.genre.UpdateGenreRequest;

import java.util.List;

public interface GenreService {
    List<GenreDto> getAll();

    GenreDto getGenreById(int id);

    GenreDto createGenre(CreateGenreRequest request);

    GenreDto updateGenre(int id, UpdateGenreRequest request);

    GenreDto deleteGenre(int id);
}
