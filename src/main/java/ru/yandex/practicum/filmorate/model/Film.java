package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;


import java.time.LocalDate;
import java.util.*;


@Setter
@Getter
@ToString
@Accessors(chain = true)
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
    private Mpa mpa;
    private Set<Genre> genres;
    private Set<Long> likes = new HashSet<>();
}
