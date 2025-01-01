package ru.yandex.practicum.filmorate.service.genre;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.genre.CreateGenreRequest;
import ru.yandex.practicum.filmorate.dto.genre.GenreDto;
import ru.yandex.practicum.filmorate.dto.genre.UpdateGenreRequest;
import ru.yandex.practicum.filmorate.exception.custom.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class GenreServiceImplement implements GenreService {
    private final GenreStorage genreStorage;

    @Override
    public List<GenreDto> getAll() {
        return genreStorage.getAllGenres()
                .stream()
                .map(GenreMapper::mapToGenreDto)
                .toList();
    }

    @Override
    public GenreDto getGenreById(int id) {
        Genre genre = genreStorage.getGenre(id).orElseThrow(() -> new NotFoundException("Жанр не найден с ID: " + id));
        return GenreMapper.mapToGenreDto(genre);
    }

    @Override
    public GenreDto createGenre(CreateGenreRequest request) {
        Genre genre = GenreMapper.mapToGenre(request);
        genre = genreStorage.addGenre(genre);
        return GenreMapper.mapToGenreDto(genre);
    }

    @Override
    public GenreDto updateGenre(int id, UpdateGenreRequest request) {
        Genre genre = genreStorage.getGenre(id)
                .orElseThrow(() -> new NotFoundException("Жанр не найден с ID: " + id));
        GenreMapper.updateGenreFields(genre, request);
        genreStorage.updateGenre(genre);
        return GenreMapper.mapToGenreDto(genre);
    }

    @Override
    public GenreDto deleteGenre(int id) {
        Genre genre = genreStorage.getGenre(id)
                .orElseThrow(() -> new NotFoundException("Жанр не найден с ID: " + id));
        genreStorage.removeGenre(id);
        return GenreMapper.mapToGenreDto(genre);
    }
}
