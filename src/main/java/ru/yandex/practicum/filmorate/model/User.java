package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;


import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Data
@ToString
@Accessors(chain = true)
//@AllArgsConstructor
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


