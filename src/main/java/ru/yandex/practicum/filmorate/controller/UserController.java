package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.ValidationException;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService = new UserService();

    //получение всех пользователей
    @ResponseBody
    @GetMapping
    public List<User> getUsers() {
        log.info("Текущее количество пользователей: {}", userService.getAllUser().size());
        return new ArrayList<>(userService.getAllUser().values());
    }

    //добавление нового пользователя
    @ResponseBody
    @PostMapping
    public User create(@Valid @RequestBody User user) throws ValidationException {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new ValidationException("Емейл не может быть пустой и емейл должен содержать @");
        }
        if (user.getLogin().isEmpty() || user.getLogin().isBlank()) {
            throw new ValidationException("Логин не может быть пустым");
        }
        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть позже текущего дня");
        }
        log.info("Вызов метода сохранения пользователя", user);
        userService.saveUser(user);
        return user;
    }

    //обновление фильма
    @ResponseBody
    @PutMapping
    public User update(@Valid @RequestBody User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new ValidationException("Емейл не может быть пустой и емейл должен содержать @");
        }
        if (user.getLogin().isEmpty() || user.getLogin().isBlank()) {
            throw new ValidationException("Логин не может быть пустым");
        }
        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть позже текущего дня");
        }
        log.info("Вызов метода обновления пользователя", user);
        if (userService.getAllUser().containsKey(user.getId())) {
            userService.updateUser(user);
        } else {
            throw new ValidationException("Нет пользователя для обновления");
        }
        return user;
    }
}
