package ru.yandex.practicum.filmorate;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class FilmorateApplication {
    public static void main(String[] args) {
        ((Logger) log).setLevel(Level.TRACE);
        try {
            log.info("Сервер Spring был запущен");
            SpringApplication.run(FilmorateApplication.class, args);
        } catch (Throwable e) {
            log.error("Непредвиденная ошибка, завершение работы программы", e);
        }
    }
}
