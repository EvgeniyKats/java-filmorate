package ru.yandex.practicum.filmorate.exception.custom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotExistException extends RuntimeException {
    public EntityNotExistException(String parameter, long id) {
        super(parameter + "id=" + id + " отсутствует в системе.");
    }
}
