package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;


import java.time.LocalDate;
import java.util.Set;

@Data
@ToString
@AllArgsConstructor
public class User {
    private Long id;
    @Email
    @NotNull
    private String email;
    @NotNull
    @NotBlank
    @NotEmpty
    private String login;
    private String name;
    @PastOrPresent
    private LocalDate birthday;
    private Set<Long> friends;
}


