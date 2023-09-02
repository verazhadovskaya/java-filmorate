package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/films")
public class FilmController {

    private final FilmService filmService = new FilmService();

    //получение всех фильмов
    @ResponseBody
    @GetMapping
    public Map<Long, Film> getFilm() {
        log.info("Текущее количество фильмов: {}", filmService.getAllFilm().size());
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
            log.info("Вызов метода сохранения фильма", film);
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
        log.info("Вызов метода обновления фильма", film);
        if (filmService.getAllFilm().containsKey(film.getId())) {
            filmService.updateFilm(film);
        } else {
            throw new ValidationException("Нет фильма для обновления");
        }
        return film;
    }

}
