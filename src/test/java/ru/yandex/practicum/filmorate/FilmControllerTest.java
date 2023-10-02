package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.*;

import java.time.LocalDate;
import java.util.HashSet;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmControllerTest {

    private UserDbStorage userStorage;
    private MpaDbStorage mpaDbStorage;
    private FilmDbStorage filmStorage;
    private FilmService filmService;
    private FilmController filmController;

    Film film = new Film();

    @BeforeEach
    void createFilm() {
        Mpa mpa = new Mpa();
        mpa.setId(1);
        mpa.setName("test");
        film.setId(1L);
        film.setName("name");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);
        film.setMpa(mpa);
        film.setGenres(new HashSet<>());
        film.setLikes(new HashSet<>());
    }


    @Test
    void createFilmValidField() {
        System.out.println(film);
        filmStorage.saveFilm(film);

        Assertions.assertFalse(filmService.getAllFilm().isEmpty());
    }

    @Test
    void updateFilmValidField() {
        filmController.create(film);
        filmController.update(film);

        Assertions.assertEquals(1, filmController.getFilm().size());
    }


    @Test
    void createFilmNotValidName() {
        film.setName("");

        Assertions.assertThrows(ValidationException.class, () -> filmController.create(film));
        Assertions.assertEquals(0, filmController.getFilm().size());
    }

    @Test
    void createFilmNotValidDescMore200() {
        film.setDescription("Очень длинное описание. Очень длинное описание. " +
                "Очень длинное описание. Очень длинное описание. " +
                "Очень длинное описание. Очень длинное описание. " +
                "Очень длинное описание. Очень длинное описание. " +
                "Очень длинное описание. \n");

        Assertions.assertThrows(ValidationException.class, () -> filmController.create(film));
        Assertions.assertEquals(0, filmController.getFilm().size());
    }

    @Test
    void createFilmNotValidDescEquals200() {
        film.setDescription("Описание ровно в 200 символов. " +
                "Описание ровно в 200 символов. Описание ровно в 200 символов. " +
                "Описание ровно в 200 символов. Описание ровно в 200 символов." +
                " Описание ровно в 200 символов. Описание ровн\n");
        filmController.create(film);

        Assertions.assertEquals(1, filmController.getFilm().size());
    }

    @Test
    void createFilmNotValidDescEquals199() {
        film.setDescription("Описание 199 символов. Описание 199 символов. " +
                "Описание 199 символов. Описание 199 символов. Описание 199 символов. " +
                "Описание 199 символов. Описание 199 символов. Описание 199 символов." +
                " Описание 199 си");
        filmController.create(film);

        Assertions.assertEquals(1, filmController.getFilm().size());
    }

    @Test
    void createFilmNotValidDescMore201() {
        film.setDescription("Описание 201 символ. Описание 201 символ. " +
                "Описание 201 символ. Описание 201 символ. Описание 201 символ. " +
                "Описание 201 символ. Описание 201 символ. " +
                "Описание 201 символ. Описание 201 символ. Описание 201\n");

        Assertions.assertThrows(ValidationException.class, () -> filmController.create(film));
        Assertions.assertEquals(0, filmController.getFilm().size());
    }

    @Test
    void createUserNotValidBirthday() {
        film.setReleaseDate(LocalDate.of(1800, 1, 1));


        Assertions.assertThrows(ValidationException.class, () -> filmController.create(film));
        Assertions.assertEquals(0, filmController.getFilm().size());
    }

    @Test
    void createUserNotValidBirthdayBorderDayMinusDay() {
        film.setReleaseDate(LocalDate.of(1895, 12, 27));


        Assertions.assertThrows(ValidationException.class, () -> filmController.create(film));
        Assertions.assertEquals(0, filmController.getFilm().size());
    }

    @Test
    void createUserNotValidBirthdayBorderDay() {
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        filmController.create(film);

        Assertions.assertEquals(1, filmController.getFilm().size());
    }

    @Test
    void createUserNotValidBirthdayBorderPlusDay() {
        film.setReleaseDate(LocalDate.of(1895, 12, 29));
        filmController.create(film);

        Assertions.assertEquals(1, filmController.getFilm().size());
    }

    @Test
    void createNotValidDuration() {
        film.setDuration(-1);

        Assertions.assertThrows(ValidationException.class, () -> filmController.create(film));
        Assertions.assertEquals(0, filmController.getFilm().size());
    }
}
