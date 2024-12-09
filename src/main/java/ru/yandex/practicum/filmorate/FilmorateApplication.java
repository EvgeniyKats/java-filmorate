package ru.yandex.practicum.filmorate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class FilmorateApplication {
    public static void main(String[] args) {
        try {
            log.info("Сервер Spring был запущен");
            SpringApplication.run(FilmorateApplication.class, args);
        } catch (Throwable e) {
            log.error("Непредвиденная ошибка, завершение работы программы", e);
        }
    }
}
