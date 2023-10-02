package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString
@Accessors(chain = true)
public class Genre {
    private int id;
    private String name;
}