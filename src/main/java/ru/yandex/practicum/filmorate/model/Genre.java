package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString
@Accessors(chain = true)
public class Genre implements Comparable<Genre>{
    private int id;
    private String name;

    @Override
    public int compareTo(Genre obj)
    {
        return (this.id - obj.id);
    }
}