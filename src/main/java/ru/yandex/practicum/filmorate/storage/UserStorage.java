package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User saveUser(User user);

    User updateUser(User user);

    List<User> getAllUser();

    User getUserById(Long id);

    void saveFriend(Long firstUserId, Long secondUserId);

    void deleteFriend(Long firstUserId, Long secondUserId);

    List<User> getListCommonFriends(Long firstUserId, Long secondUserId);

    List<User> getFriends(Long userId);
}
