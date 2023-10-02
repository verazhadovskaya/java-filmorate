package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserControllerTest {
    private final UserDbStorage userStorage;
    private final UserController userController;
    User user = new User();

    @BeforeEach
    void createUser() {
        user.setEmail("test@test.ru");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        userStorage.deleteAllUser();
    }

    @Test
    void createUserValidField() {
        userStorage.saveUser(user);
        Assertions.assertEquals(1, userStorage.getAllUser().size());
    }

    @Test
    void updateUserValidField() {
        userStorage.saveUser(user);
        userStorage.updateUser(user);

        Assertions.assertEquals(1, userStorage.getAllUser().size());
    }

    @Test
    void createUserNotValidEmail() {
        user.setEmail("testtest.ru");

        Assertions.assertThrows(ValidationException.class, () -> userController.create(user));
        Assertions.assertEquals(0, userStorage.getAllUser().size());
    }

    @Test
    void createUserNotValidLogin() {
        user.setLogin("");

        Assertions.assertThrows(ValidationException.class, () -> userController.create(user));
        Assertions.assertEquals(0, userStorage.getAllUser().size());
    }

    @Test
    void createUserNotValidBirthday() {
        user.setBirthday(LocalDate.of(2099, 1, 1));

        Assertions.assertThrows(ValidationException.class, () -> userController.create(user));
        Assertions.assertEquals(0, userStorage.getAllUser().size());
    }

    @Test
    void createUserNotValidBirthdayNowPlusDay() {
        user.setBirthday(LocalDate.now().plusDays(1));

        Assertions.assertThrows(ValidationException.class, () -> userController.create(user));
        Assertions.assertEquals(0, userStorage.getAllUser().size());
    }

    @Test
    void createUserValidBirthdayNowMinusDay() {
        user.setBirthday(LocalDate.now().minusDays(1));
        userStorage.saveUser(user);

        Assertions.assertEquals(1, userStorage.getAllUser().size());
    }

    @Test
    void createUserValidBirthdayNow() {
        user.setBirthday(LocalDate.now());
        userStorage.saveUser(user);

        Assertions.assertEquals(1, userStorage.getAllUser().size());
    }

    @Test
    void createEmptyName() {
        user.setName(null);
        userStorage.saveUser(user);

        Assertions.assertEquals(user.getName(), user.getLogin());
        Assertions.assertEquals(1, userStorage.getAllUser().size());
    }
}
