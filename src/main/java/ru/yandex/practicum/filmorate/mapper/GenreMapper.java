package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.genre.CreateGenreRequest;
import ru.yandex.practicum.filmorate.dto.genre.GenreDTO;
import ru.yandex.practicum.filmorate.dto.genre.UpdateGenreRequest;
import ru.yandex.practicum.filmorate.model.Genre;

public class GenreMapper {

    private GenreMapper() {
    }

    public static Genre mapToGenre(CreateGenreRequest request) {
        Genre genre = new Genre();
        genre.setName(request.getName());
        return genre;
    }

    public static GenreDTO mapToGenreDto(Genre genre) {
        GenreDTO dto = new GenreDTO();
        dto.setId(genre.getId());
        dto.setName(genre.getName());
        return dto;
    }

    public static Genre updateGenreFields(Genre genre, UpdateGenreRequest request) {
        if (request.hasName()) {
            genre.setName(request.getName());
        }
        return genre;
    }
}
