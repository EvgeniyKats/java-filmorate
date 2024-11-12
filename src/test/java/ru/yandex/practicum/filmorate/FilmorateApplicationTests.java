package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.net.http.HttpClient;
import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmorateApplicationTests {
    private HttpClient client;
    private final String BASE_URI = "http://localhost:8080";
    private final String FILM_URI = "/films";

    @BeforeEach
    void beforeEach() {
        client = HttpClient.newHttpClient();
    }

    @AfterEach
    void afterEach() {
        client.close();
    }

    @Test
    void contextLoads() {

    }
}
