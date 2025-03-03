package ru.yandex.practicum.filmorate.service.ratingmpa;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.ratingmpa.RatingMpaDto;
import ru.yandex.practicum.filmorate.exception.custom.NotFoundException;
import ru.yandex.practicum.filmorate.dto.mapper.RatingMpaMapper;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.storage.ratingmpa.RatingMpaStorage;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class RatingMpaServiceImplement implements RatingMpaService {
    private RatingMpaStorage ratingMpaStorage;

    @Override
    public RatingMpaDto getMpaById(long idMpa) {
        RatingMpa ratingMpa = ratingMpaStorage.getRatingMPAById(idMpa)
                .orElseThrow(() -> new NotFoundException("Rating mpa не найден с ID = " + idMpa));
        log.info("getMpaById success");
        return RatingMpaMapper.mapToRatingMpaDto(ratingMpa);
    }

    @Override
    public List<RatingMpaDto> getAllMpa() {
        List<RatingMpaDto> result = ratingMpaStorage.getAll().stream()
                .map(RatingMpaMapper::mapToRatingMpaDto)
                .toList();
        log.info("getAllMpa success");
        return result;
    }
}
