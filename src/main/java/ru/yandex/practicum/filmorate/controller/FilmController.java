package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    //получение всех фильмов
    @ResponseBody
    @GetMapping
    public List<Film> getFilm() {
        return filmService.getAllFilm();
    }

    //добавление нового фильма
    @ResponseBody
    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(
                LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не может быть раньше 28.12.1985");
        }
        if (film.getName().isEmpty() || film.getName().isBlank()) {
            throw new ValidationException("Наименование фильма не может быть пустым");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжить фильма должны быть больше 0");
        }
        if (film.getDescription().length() > 200 || film.getDescription().length() == 0) {
            throw new ValidationException("Описание фильма не может быть больше 200 символов");
        }
        filmService.saveFilm(film);
        return film;
    }

    //обновление фильма
    @ResponseBody
    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(
                LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не может быть раньше 28.12.1985");
        }
        if (film.getName().isEmpty() || film.getName().isBlank()) {
            throw new ValidationException("Наименование фильма не может быть пустым");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжить фильма должны быть больше 0");
        }
        if (film.getDescription().length() > 200 || film.getDescription().length() == 0) {
            throw new ValidationException("Описание фильма не может быть больше 200 символов");
        }
        filmService.updateFilm(film);
        return film;
    }

    @ResponseBody
    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") Long filmId, @PathVariable("userId") Long userId) {
        filmService.addLike(filmId, userId);
    }

    @ResponseBody
    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") Long filmId, @PathVariable("userId") Long userId) {
        filmService.deleteLike(filmId, userId);
    }

    @ResponseBody
    @GetMapping("/popular")
    public List<Film> getTopFilms(@RequestParam(required = false, defaultValue = "10") Integer count) {
        return filmService.getTopFilms(count);
    }

    @GetMapping("/{id}")
    public Film get(@PathVariable("id") long id) {
        return filmService.getFilmById(id);
    }


}
