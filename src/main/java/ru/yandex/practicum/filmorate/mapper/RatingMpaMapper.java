package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.ratingmpa.RatingMpaDto;
import ru.yandex.practicum.filmorate.model.RatingMpa;

public class RatingMpaMapper {

    private RatingMpaMapper() {
    }

    public static RatingMpaDto mapToRatingMpaDto(RatingMpa ratingMpa) {
        RatingMpaDto result = new RatingMpaDto();
        result.setId(ratingMpa.getId());
        result.setName(ratingMpa.getName());
        return result;
    }

    public static RatingMpa mapToRatingMpa(RatingMpaDto dto) {
        RatingMpa result = new RatingMpa();
        result.setId(dto.getId());
        result.setName(dto.getName());
        return result;
    }
}
