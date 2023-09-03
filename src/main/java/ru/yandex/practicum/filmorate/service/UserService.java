package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class UserService {
    private final Map<Long, User> users = new HashMap<>();
    private long nextId = 1;

    public User saveUser(User user) {
        log.info("Вызов метода сохранения пользователя", user);
        user.setId(nextId++);
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }

    public User updateUser(User user) {
        log.info("Вызов метода обновления пользователя", user);
        if (users.containsKey(user.getId())) {
            if (user.getName() == null) {
                user.setName(user.getLogin());
            }
            users.put(user.getId(), user);
        } else {
            throw new ValidationException("Нет пользователя для обновления");
        }
        return user;
    }

    public List<User> getAllUser() {
        log.info("Вызов метода получения всех пользователей. Текущее количество: {}", users.size());
        return new ArrayList<>(users.values());
    }
}
