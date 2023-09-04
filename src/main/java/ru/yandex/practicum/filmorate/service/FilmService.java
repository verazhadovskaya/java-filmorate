package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class FilmService {
    private final Map<Long, Film> films = new HashMap<>();
    private long nextId = 1;

    public Film saveFilm(Film film) {
        log.info("Вызов метода сохранения фильма", film);
        film.setId(nextId++);
        films.put(film.getId(), film);
        return film;
    }

    public Film updateFilm(Film film) {
        log.info("Вызов метода обновления фильма", film);
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            throw new ValidationException("Нет фильма для обновления");
        }
        return film;
    }

    public List<Film> getAllFilm() {
        log.info("Вызов метода получения всех фильмов. Текущее количество: {}", films.size());
        return new ArrayList<>(films.values());
    }
}
