package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Objects;

@Setter
@Getter
@Accessors(chain = true)
public class Genre implements Comparable<Genre> {
    private int id;
    private String name;

    @Override
    public int compareTo(Genre obj) {
        return (this.id - obj.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object ob) {
        if (ob == this) {
            return true;
        }
        if (ob == null || ob.getClass() != getClass()) {
            return false;
        }
        Genre g = (Genre) ob;
        return Objects.equals(id, g.id);
    }
}