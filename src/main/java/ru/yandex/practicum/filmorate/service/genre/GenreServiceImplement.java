package ru.yandex.practicum.filmorate.service.genre;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.genre.CreateGenreDto;
import ru.yandex.practicum.filmorate.dto.genre.GenreDto;
import ru.yandex.practicum.filmorate.dto.genre.UpdateGenreDto;
import ru.yandex.practicum.filmorate.exception.custom.NotFoundException;
import ru.yandex.practicum.filmorate.dto.mapper.GenreMapper;
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
        log.info("getGenreById success");
        return GenreMapper.mapToGenreDto(genre);
    }

    @Override
    public GenreDto createGenre(CreateGenreDto request) {
        log.trace("CreateGenreRequest = {}", request);
        Genre genre = GenreMapper.mapToGenre(request);
        log.trace("mapToGenre, {}", genre);
        genre = genreStorage.addGenre(genre);
        log.info("createGenre success");
        return GenreMapper.mapToGenreDto(genre);
    }

    @Override
    public GenreDto updateGenre(int id, UpdateGenreDto request) {
        log.trace("UpdateGenreRequest = {}", request);
        Genre genre = genreStorage.getGenre(id)
                .orElseThrow(() -> new NotFoundException("Жанр не найден с ID: " + id));
        genre.updateFieldsFromUpdateDto(request);
        log.trace("updateGenreFields, {}", genre);
        genreStorage.updateGenre(genre);
        log.info("updateGenre success");
        return GenreMapper.mapToGenreDto(genre);
    }

    @Override
    public GenreDto deleteGenre(int id) {
        Genre genre = genreStorage.getGenre(id)
                .orElseThrow(() -> new NotFoundException("Жанр не найден с ID: " + id));
        genreStorage.removeGenre(id);
        log.info("deleteGenre success");
        return GenreMapper.mapToGenreDto(genre);
    }
}
