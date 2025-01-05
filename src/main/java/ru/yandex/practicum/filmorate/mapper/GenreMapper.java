package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.genre.CreateGenreDto;
import ru.yandex.practicum.filmorate.dto.genre.GenreDto;
import ru.yandex.practicum.filmorate.dto.genre.UpdateGenreDto;
import ru.yandex.practicum.filmorate.model.Genre;

public class GenreMapper {

    private GenreMapper() {
    }

    public static Genre mapToGenre(CreateGenreDto request) {
        Genre genre = new Genre();
        genre.setName(request.getName());
        return genre;
    }

    public static Genre mapToGenre(GenreDto genreDto) {
        Genre genre = new Genre();
        genre.setId(genreDto.getId());
        genre.setName(genreDto.getName());
        return genre;
    }

    public static GenreDto mapToGenreDto(Genre genre) {
        GenreDto dto = new GenreDto();
        dto.setId(genre.getId());
        dto.setName(genre.getName());
        return dto;
    }

    public static Genre updateGenreFields(Genre genre, UpdateGenreDto request) {
        if (request.hasName()) {
            genre.setName(request.getName());
        }
        return genre;
    }
}
