package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;


import java.util.List;

public interface FilmStorage {

    Film saveFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getAllFilm();

    void saveLike(Long filmId, Long userId);

    void deleteLike(Long filmId, Long userId);

    List<Film> getTopFilms(int count);

    Film getFilmById(Long id);

}
