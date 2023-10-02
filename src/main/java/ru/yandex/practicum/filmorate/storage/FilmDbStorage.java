package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;


import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final UserDbStorage userStorage;

    private static final String INSERT_FILM_QUERY = "insert into films(id, name, description," +
            "release_date, duration, rating_id) values (nextval('film_seq'),?,?,?,?,?)";

    private static final String SELECT_FILM_ID_QUERY = "select id from films order by id desc limit 1";
    private static final String UPDATE_FILM_QUERY = "update films set name=?, description=?, release_date=?," +
            " duration=?, rating_id=? where id =?";
    private static final String SELECT_FILM_QUERY_BY_ID = "select f.*,r.id as rating_id, r.rating_name from films f " +
            "left join rating r on f.rating_id = r.id " +
            "where f.id =?";
    private static final String SELECT_ALL_FILM_QUERY = "select f.*, r.id as rating_id, r.rating_name from films f " +
            "left join rating r on f.rating_id = r.id";
    private static final String SAVE_LIKE_QUERY = "insert into film_likes(id, film_id, user_id) " +
            "values (nextval('likes_seq'),?,?)";

    private static final String SELECT_FILM_GENRE_QUERY = "select g.id as genre_id, g.genre_name from films f " +
            "left join film_genre fg on f.id = fg.film_id " +
            "left join genre g on fg.genre_id = g.id " +
            "where f.id=? order by g.id";

    private static final String SELECT_TOP_FILM_QUERY = "select f.*,r.id as rating_id, r.rating_name from films f " +
            "left join rating r on f.rating_id = r.id " +
            "left join (select f.id, count(fl.id) as count_likes from films f " +
            "left join film_likes fl on f.id= fl.film_id " +
            "group by f.id) a ON f.id=a.id order by a.count_likes desc limit ?;";

    private static final String DELETE_LIKE_QUERY = "delete from film_likes where film_id =?";
    private static final String INSERT_FILM_GENRE_QUERY = "insert into film_genre (id, film_id, genre_id) values (nextval('film_genre_seq'),?,?)";
    private static final String DELETE_FILM_GENRE_QUERY = "delete from film_genre where film_id = ?";
    private static final String DELETE_ALL_FILM_QUERY = "delete from films";

    private final JdbcTemplate jdbcTemplate;
    private final FilmRowMapper filmRowMapper;
    private final GenreRowMapper genreRowMapper;

    @Override
    public Film saveFilm(Film film) {
        int ratingId = film.getMpa().getId();
        jdbcTemplate.update(INSERT_FILM_QUERY,
                film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), ratingId);
        Long id = jdbcTemplate.queryForObject(SELECT_FILM_ID_QUERY, Long.class);
        film.setId(id);
        jdbcTemplate.update(DELETE_FILM_GENRE_QUERY, film.getId());
        Set<Genre> genres = film.getGenres();
        if (genres != null) {
            for (Genre genre : genres) {
                jdbcTemplate.update(INSERT_FILM_GENRE_QUERY, film.getId(), genre.getId());
            }
        }
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (getFilmById(film.getId()) != null) {
            Set<Genre> genres = film.getGenres();
            int ratingId = film.getMpa().getId();
            jdbcTemplate.update(UPDATE_FILM_QUERY, film.getName(), film.getDescription(),
                    film.getReleaseDate(), film.getDuration(), ratingId, film.getId());
            jdbcTemplate.update(DELETE_FILM_GENRE_QUERY, film.getId());
            if (genres != null) {
                for (Genre genre : genres) {
                    jdbcTemplate.update(INSERT_FILM_GENRE_QUERY, film.getId(), genre.getId());
                }
            }
            List<Genre> listGenres = new ArrayList<>();
            Set<Genre> setGenres = new HashSet<>();
            listGenres = jdbcTemplate.query(SELECT_FILM_GENRE_QUERY, ps -> ps.setLong(1, film.getId()), genreRowMapper);
            if (listGenres.get(0).getId() != 0) {
                for (Genre oneGenre : listGenres) {
                    setGenres.add(oneGenre);
                }
                setGenres=setGenres.stream()
                        .collect(Collectors.toSet());
                film.setGenres(setGenres);
            }
        } else {
            throw new ObjectNotFoundException("Нет фильма для обновления");
        }

        return film;
    }

    @Override
    public List<Film> getAllFilm() {
        List<Film> films = jdbcTemplate.query(SELECT_ALL_FILM_QUERY, filmRowMapper);
        List<Film> wholeFilms = new ArrayList<>();
        for (Film film : films) {
            List<Genre> listGenres = new ArrayList<>();
            Set<Genre> genres = new HashSet<>();
            listGenres = jdbcTemplate.query(SELECT_FILM_GENRE_QUERY, ps -> ps.setLong(1, film.getId()), genreRowMapper);
            if (listGenres.get(0).getId() != 0) {
                for (Genre oneGenre : listGenres) {
                    genres.add(oneGenre);
                }
                genres=genres.stream()
                        .collect(Collectors.toSet());
                film.setGenres(genres);
            }
            wholeFilms.add(film);
        }
        return wholeFilms;
    }

    @Override
    public void saveLike(Long filmId, Long userId) {
        if (getFilmById(filmId) != null) {
            jdbcTemplate.update(SAVE_LIKE_QUERY, filmId, userId);
        } else {
            throw new ObjectNotFoundException("Нет фильма с переданным ИД");
        }
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        if (getFilmById(filmId) != null && userStorage.getUserById(userId) != null) {
            jdbcTemplate.update(DELETE_LIKE_QUERY, filmId);
        } else {
            throw new ObjectNotFoundException("Нет фильма с переданным ИД");
        }
    }

    @Override
    public List<Film> getTopFilms(int count) {
        return jdbcTemplate.query(SELECT_TOP_FILM_QUERY, ps -> ps.setInt(1, count), filmRowMapper);
    }

    @Override
    public Film getFilmById(Long id) {
        Film film = jdbcTemplate
                .query(SELECT_FILM_QUERY_BY_ID, ps -> ps.setLong(1, id), filmRowMapper)
                .stream()
                .findFirst()
                .orElse(null);
        Set<Genre> genres = new HashSet<>();
        List<Genre> listGenres = new ArrayList<>();
        if (film != null) {
            listGenres = jdbcTemplate.query(SELECT_FILM_GENRE_QUERY, ps -> ps.setLong(1, film.getId()), genreRowMapper);
            if (listGenres.get(0).getId() != 0) {
                for (Genre oneGenre : listGenres) {
                    genres.add(oneGenre);
                }
                genres=genres.stream()
                        .collect(Collectors.toSet());
                film.setGenres(genres);
            }
        }
        if (film != null) {
            return film;
        } else {
            throw new ObjectNotFoundException("Нет фильма с таким ИД");
        }
    }

    public void deleteAllFilm() {
        jdbcTemplate.update(DELETE_ALL_FILM_QUERY);
    }
}
