package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.ratingmpa.RatingMpaDto;
import ru.yandex.practicum.filmorate.service.ratingmpa.RatingMpaService;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/mpa")
public class RatingMpaController {
    private RatingMpaService ratingMpaService;

    @GetMapping("/{id}")
    public RatingMpaDto getMpaById(@PathVariable Integer id) {
        return ratingMpaService.getMpaById(id);
    }

    @GetMapping
    public List<RatingMpaDto> getAllMpa() {
        return ratingMpaService.getAllMpa();
    }
}
