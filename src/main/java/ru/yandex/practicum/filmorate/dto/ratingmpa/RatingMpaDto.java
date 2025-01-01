package ru.yandex.practicum.filmorate.dto.ratingmpa;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RatingMpaDto {
    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int id;
    private String name;
}
