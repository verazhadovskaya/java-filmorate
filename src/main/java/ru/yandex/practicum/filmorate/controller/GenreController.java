package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {
    @Autowired
    private final GenreService genreService;

    @GetMapping("/{id}")
    public Genre get(@PathVariable("id") int id) {
        return genreService.getGenreById(id);
    }

    @ResponseBody
    @GetMapping
    public List<Genre> getGenre() {
        return genreService.getGenre();
    }
}
