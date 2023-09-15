package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

@SpringBootTest
@RequiredArgsConstructor
public class UserControllerTest {
    private UserStorage userStorage = new InMemoryUserStorage();
    private UserService userService = new UserService(userStorage);
    private UserController userController = new UserController(userService);
    User user = new User(1L, "test@test.ru", "login", "name", LocalDate.of(1990, 1, 1), null);

    @BeforeEach
    void createUser() {
        user.setEmail("test@test.ru");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.of(1990, 1, 1));
    }

    @Test
    void createUserValidField() {
        userController.create(user);
        Assertions.assertEquals(1, userController.getUsers().size());
    }

    @Test
    void updateUserValidField() {
        userController.create(user);
        userController.update(user);

        Assertions.assertEquals(1, userController.getUsers().size());
    }

    @Test
    void createUserNotValidEmail() {
        user.setEmail("testtest.ru");

        Assertions.assertThrows(ValidationException.class, () -> userController.create(user));
        Assertions.assertEquals(0, userController.getUsers().size());
    }

    @Test
    void createUserNotValidLogin() {
        user.setLogin("");

        Assertions.assertThrows(ValidationException.class, () -> userController.create(user));
        Assertions.assertEquals(0, userController.getUsers().size());
    }

    @Test
    void createUserNotValidBirthday() {
        user.setBirthday(LocalDate.of(2099, 1, 1));

        Assertions.assertThrows(ValidationException.class, () -> userController.create(user));
        Assertions.assertEquals(0, userController.getUsers().size());
    }

    @Test
    void createUserNotValidBirthdayNowPlusDay() {
        user.setBirthday(LocalDate.now().plusDays(1));

        Assertions.assertThrows(ValidationException.class, () -> userController.create(user));
        Assertions.assertEquals(0, userController.getUsers().size());
    }

    @Test
    void createUserValidBirthdayNowMinusDay() {
        user.setBirthday(LocalDate.now().minusDays(1));
        userController.create(user);

        Assertions.assertEquals(1, userController.getUsers().size());
    }

    @Test
    void createUserValidBirthdayNow() {
        user.setBirthday(LocalDate.now());
        userController.create(user);

        Assertions.assertEquals(1, userController.getUsers().size());
    }

    @Test
    void createEmptyName() {
        user.setName(null);
        userController.create(user);

        Assertions.assertEquals(user.getName(), user.getLogin());
        Assertions.assertEquals(1, userController.getUsers().size());
    }
}
