package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

@Component
public class FilmRowMapper implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Film()
                .setId(rs.getLong("id"))
                .setName(rs.getString("name"))
                .setDescription(rs.getString("description"))
                .setReleaseDate(rs.getDate("release_date").toLocalDate())
                .setDuration(rs.getInt("duration"))
                .setMpa(new Mpa()
                        .setId(rs.getInt("rating_id"))
                        .setName(rs.getString("rating_name"))
                )
                .setGenres(new HashSet<>());
    }
}
