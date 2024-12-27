package ru.yandex.practicum.filmorate.service.genre;

import ru.yandex.practicum.filmorate.dto.genre.CreateGenreRequest;
import ru.yandex.practicum.filmorate.dto.genre.GenreDTO;
import ru.yandex.practicum.filmorate.dto.genre.UpdateGenreRequest;

import java.util.List;

public interface GenreService {
    List<GenreDTO> getAll();

    GenreDTO getGenreById(int id);

    GenreDTO createGenre(CreateGenreRequest request);

    GenreDTO updateGenre(int id, UpdateGenreRequest request);

    GenreDTO deleteGenre(int id);
}
