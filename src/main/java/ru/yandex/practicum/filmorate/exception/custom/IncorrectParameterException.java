package ru.yandex.practicum.filmorate.exception.custom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IncorrectParameterException extends IllegalArgumentException {
    public IncorrectParameterException(String parameter, String reason) {
        super("Параметр: " + parameter + " Ошибка: " + reason);
    }
}
