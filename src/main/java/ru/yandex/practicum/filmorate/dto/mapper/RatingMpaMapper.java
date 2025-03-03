package ru.yandex.practicum.filmorate.dto.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import ru.yandex.practicum.filmorate.dto.ratingmpa.RatingMpaDto;
import ru.yandex.practicum.filmorate.model.RatingMpa;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RatingMpaMapper {
    public static RatingMpaDto mapToRatingMpaDto(RatingMpa ratingMpa) {
        RatingMpaDto result = new RatingMpaDto();
        result.setId(ratingMpa.getId());
        result.setName(ratingMpa.getName());
        return result;
    }

    public static Integer mapToRatingMpa(RatingMpaDto dto) {
        return dto.getId();
    }
}
