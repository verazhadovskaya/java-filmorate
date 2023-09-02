package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import lombok.ToString;


import java.time.LocalDate;

@Data
@ToString
public class Film {
    private Long id;
    @NotEmpty
    @NotNull
    @NotBlank
    private String name;
    @Size(min = 1, max = 200)
    private String description;
    private LocalDate releaseDate;
    @Min(0)
    private int duration;
}
