package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class UserService {
    private final Map<Long, User> users = new HashMap<>();
    private long nextId = 1;

    public User saveUser(User user) {
        user.setId(nextId++);
        if (user.getName()==null){
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }
    public User updateUser(User user) {
        if (user.getName()==null){
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }

    public Map<Long, User> getAllUser() {
        return users;
    }
}
