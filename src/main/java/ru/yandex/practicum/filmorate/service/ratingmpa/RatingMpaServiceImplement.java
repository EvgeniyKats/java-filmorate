package ru.yandex.practicum.filmorate.service.ratingmpa;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.ratingmpa.RatingMpaDto;

import java.util.List;

@Service
public class RatingMpaServiceImplement implements RatingMpaService {
    @Override
    public RatingMpaDto getMpaById(long idMpa) {
        return null;
    }

    @Override
    public List<RatingMpaDto> getAllMpa() {
        return List.of();
    }
}
