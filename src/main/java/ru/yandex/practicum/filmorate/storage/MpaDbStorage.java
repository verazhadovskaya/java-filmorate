package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MpaDbStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MpaRowMapper mpaRowMapper;
    private static final String SELECT_MPA_BY_ID = "select * from rating where id =?";

    private static final String SELECT_MPA = "select * from rating";

    public Mpa getMpaById(int id) {
        Mpa mpa = jdbcTemplate
                .query(SELECT_MPA_BY_ID, ps -> ps.setInt(1, id), mpaRowMapper)
                .stream()
                .findFirst()
                .orElse(null);
        if (mpa != null) {
            return mpa;
        } else {
            throw new ObjectNotFoundException("Нет рейтинга с таким ИД");
        }
    }

    public List<Mpa> getAllMpa() {
        return jdbcTemplate.query(SELECT_MPA, mpaRowMapper);
    }
}
