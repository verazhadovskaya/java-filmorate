package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@SpringBootTest
public class UserControllerTest {
    UserController userController = new UserController();
    User user = new User();

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
        user.setBirthday(LocalDate.of(2099,1,1));

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
