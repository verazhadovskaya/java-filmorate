package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class GenreDbStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreRowMapper genreRowMapper;

    private static final String SELECT_GENRE_BY_ID = "select * from genre where id =?";

    private static final String SELECT_GENRE = "select * from genre";

    public Genre getGenreById(int id) {
        Genre genre = jdbcTemplate
                .query(SELECT_GENRE_BY_ID, ps -> ps.setInt(1, id), genreRowMapper)
                .stream()
                .findFirst()
                .orElse(null);
        if (genre != null) {
            return genre;
        } else {
            throw new ObjectNotFoundException("Нет жанра с таким ИД");
        }
    }

    public List<Genre> getAllGenre() {
        return jdbcTemplate.query(SELECT_GENRE, genreRowMapper);
    }
}
