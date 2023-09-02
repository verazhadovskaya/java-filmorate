package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class FilmService {
    private final Map<Long, Film> films = new HashMap<>();
    private long nextId = 1;

    public Film saveFilm(Film film) {
        film.setId(nextId++);
        films.put(film.getId(), film);
        return film;
    }

    public Film updateFilm(Film film) {
        films.put(film.getId(), film);
        return film;
    }
    
    public Map<Long, Film> getAllFilm() {
        return films;
    }
}
