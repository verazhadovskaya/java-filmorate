package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {
    @Qualifier("genreDbStorage")
    private final GenreDbStorage genreDbStorage;

    public Genre getGenreById(int id) {
        return genreDbStorage.getGenreById(id);
    }

    public List<Genre> getGenre() {
        return genreDbStorage.getAllGenre();
    }
}
