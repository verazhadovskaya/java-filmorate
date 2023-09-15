package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private long nextId = 1;

    @Override
    public Film saveFilm(Film film) {
        log.info("Вызов метода сохранения фильма", film);
        film.setId(nextId++);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        log.info("Вызов метода обновления фильма", film);
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            throw new ObjectNotFoundException("Нет фильма для обновления");
        }
        return film;
    }

    @Override
    public List<Film> getAllFilm() {
        log.info("Вызов метода получения всех фильмов. Текущее количество: {}", films.size());
        return new ArrayList<>(films.values());
    }

    @Override
    public void saveLike(Long filmId, Long userId) {
        log.info("Вызов метода добавления лайка в фильм");
        if (films.containsKey(filmId)) {
            Film film = films.get(filmId);
            Set<Long> likes = new HashSet<Long>();
            if (film.getLikes() != null) {
                likes = film.getLikes();
            }
            likes.add(userId);
            film.setLikes(likes);
        } else {
            throw new ObjectNotFoundException("Нет фильма с переданным ИД");
        }
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        log.info("Вызов метода удаления лайка из фильма");
        if (films.containsKey(filmId)) {
            Film film = films.get(filmId);
            Set<Long> likes = new HashSet<Long>();
            if (film.getLikes() != null) {
                likes = film.getLikes();
            }
            likes.remove(userId);
            film.setLikes(likes);
        } else {
            throw new ObjectNotFoundException("Нет фильма с переданным ИД");
        }
    }

    @Override
    public List<Film> getTopFilms(int count) {
        log.info("Вызов метода получения ТОП фильмов");
        return films.values()
                .stream()
                .sorted(Comparator.comparing(film -> film.getLikes().size(), Comparator.reverseOrder()))
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public Film getFilmById(Long id) {
        if (films.get(id) != null) {
            return films.get(id);
        } else {
            throw new ObjectNotFoundException("Нет фильма с переданным ИД");
        }
    }
}
