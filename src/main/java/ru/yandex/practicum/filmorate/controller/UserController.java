package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;

    //получение всех пользователей
    @ResponseBody
    @GetMapping
    public List<User> getUsers() {
        return userService.getAllUser();
    }

    //добавление нового пользователя
    @ResponseBody
    @PostMapping
    public User create(@Valid @RequestBody User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new ValidationException("Емейл не может быть пустой и емейл должен содержать @");
        }
        if (user.getLogin().isEmpty() || user.getLogin().isBlank()) {
            throw new ValidationException("Логин не может быть пустым");
        }
        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть позже текущего дня");
        }
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
        userService.updateUser(user);
        return user;
    }

    @GetMapping("/{id}")
    public User get(@PathVariable("id") long id) {
        return userService.getUserById(id);
    }

    @ResponseBody
    @PutMapping("/{id}/friends/{friendId}")
    public void addFriends(@PathVariable("id") Long id, @PathVariable("friendId") Long friendId) {
        userService.addFriend(id, friendId);
    }

    @ResponseBody
    @PutMapping("/{id}/friends/{friendId}/approve")
    public void approveFriends(@PathVariable("id") Long id, @PathVariable("friendId") Long friendId) {
        userService.approveFriend(id, friendId);
    }

    @ResponseBody
    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriends(@PathVariable("id") Long id, @PathVariable("friendId") Long friendId) {
        userService.deleteFriend(id, friendId);
    }

    @ResponseBody
    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getListCommonFriends(@PathVariable("id") Long id, @PathVariable("otherId") Long otherId) {
        return userService.getListCommonFriends(id, otherId);
    }

    @ResponseBody
    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable("id") Long id) {
        return userService.getFriends(id);
    }
}
