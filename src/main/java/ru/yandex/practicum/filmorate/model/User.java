package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;


import java.time.LocalDate;
import java.util.Set;

@Setter
@Getter
@ToString
@Accessors(chain = true)
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


