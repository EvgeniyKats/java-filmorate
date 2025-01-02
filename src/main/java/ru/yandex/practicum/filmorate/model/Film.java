package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validate.Create;
import ru.yandex.practicum.filmorate.validate.Update;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder(toBuilder = true)
public class Film {
    @NotNull(groups = Update.class)
    private Long id;
    @NotBlank(groups = {Create.class})
    private String name;
    @Size(groups = {Create.class, Update.class}, max = 200)
    private String description;
    @NotNull(groups = Create.class)
    private LocalDate releaseDate;
    @Positive(groups = {Create.class, Update.class})
    private Integer duration;
    private RatingMpa mpa;
    private final Set<Long> filmLikesByUserId = new HashSet<>();
    private final Set<Genre> genres = new LinkedHashSet<>();

    public long getCountLikes() {
        return filmLikesByUserId.size();
    }

    public void addLike(long id) {
        filmLikesByUserId.add(id);
    }

    public void removeLike(long id) {
        filmLikesByUserId.remove(id);
    }

    public List<Long> getFilmLikesByUserId() {
        return new ArrayList<>(filmLikesByUserId);
    }

    public void addGenre(Genre genre) {
        genres.add(genre);
    }

    public void removeGenre(Genre genre) {
        genres.remove(genre);
    }
}
