package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.dto.genre.UpdateGenreDto;

@Data
@EqualsAndHashCode(of = "id")
public class Genre {
    private int id;
    private String name;

    public void updateFieldsFromUpdateDto(UpdateGenreDto request) {
        if (request.hasName()) name = request.getName();
    }
}
