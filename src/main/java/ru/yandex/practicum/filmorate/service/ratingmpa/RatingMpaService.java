package ru.yandex.practicum.filmorate.service.ratingmpa;

import ru.yandex.practicum.filmorate.dto.ratingmpa.RatingMpaDto;

import java.util.List;

public interface RatingMpaService {
    RatingMpaDto getMpaById(long idMpa);

    List<RatingMpaDto> getAllMpa();
}
