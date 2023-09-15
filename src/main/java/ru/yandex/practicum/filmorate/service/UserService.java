package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public User saveUser(User user) {
        return userStorage.saveUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public List<User> getAllUser() {
        return userStorage.getAllUser();
    }

    public User getUserById(Long id) {
        return userStorage.getUserById(id);
    }

    public void addFriend(Long firstUserId, Long secondUserId) {
        userStorage.saveFriend(firstUserId, secondUserId);
        userStorage.saveFriend(secondUserId, firstUserId);
    }

    public void deleteFriend(Long firstUserId, Long secondUserId) {
        userStorage.deleteFriend(firstUserId, secondUserId);
        userStorage.deleteFriend(secondUserId, firstUserId);
    }

    public List<User> getListCommonFriends(Long userId, Long secondUserId) {
        return userStorage.getListCommonFriends(userId, secondUserId);
    }

    public List<User> getFriends(Long userId) {
        return userStorage.getFriends(userId);
    }
}
