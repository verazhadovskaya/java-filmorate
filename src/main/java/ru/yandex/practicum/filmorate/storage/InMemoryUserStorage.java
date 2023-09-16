package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long nextId = 1L;

    @Override
    public User saveUser(User user) {
        log.info("Вызов метода сохранения пользователя", user);
        user.setId(nextId++);
        if (StringUtils.hasText(user.getName())) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        log.info("Вызов метода обновления пользователя", user);
        if (users.containsKey(user.getId())) {
            if (StringUtils.hasText(user.getName())) {
                user.setName(user.getLogin());
            }
            users.put(user.getId(), user);
        } else {
            throw new ObjectNotFoundException("Нет пользователя для обновления");
        }
        return user;
    }

    @Override
    public List<User> getAllUser() {
        log.info("Вызов метода получения всех пользователей. Текущее количество: {}", users.size());
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(Long id) {
        log.info("Вызов метода получения пользователя по ИД = {}", id);
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new ObjectNotFoundException("Нет пользователя с таким ИД");
        }
    }

    @Override
    public void saveFriend(Long firstUserId, Long secondUserId) {
        log.info("Вызов метода добавления  пользователя в друзья, firstUserId = {}, secondUserId = {}", firstUserId, secondUserId);
        if (users.containsKey(firstUserId) && users.containsKey(secondUserId)) {
            User user = users.get(firstUserId);
            Set<Long> userFriend = new HashSet<Long>();
            if (user.getFriends() != null) {
                userFriend = user.getFriends();
            }
            userFriend.add(secondUserId);
            user.setFriends(userFriend);
        } else {
            throw new ObjectNotFoundException("Нет пользователя (-ей) с переданным ИД");
        }
    }

    @Override
    public void deleteFriend(Long firstUserId, Long secondUserId) {
        log.info("Вызов метода удаления пользователя из друзей, firstUserId = {}, secondUserId = {}", firstUserId, secondUserId);
        if (users.containsKey(firstUserId) && users.containsKey(secondUserId)) {
            User user = users.get(firstUserId);
            Set<Long> userFriend = new HashSet<Long>();
            if (user.getFriends() != null) {
                userFriend = user.getFriends();
            }
            if (userFriend.contains(secondUserId)) {
                userFriend.remove(secondUserId);
                user.setFriends(userFriend);
            } else {
                throw new ObjectNotFoundException("Пользователи с переданными ИД не являются друзьями");
            }
        } else {
            throw new ObjectNotFoundException("Нет пользователя (-ей) с переданным ИД");
        }
    }

    @Override
    public List<User> getListCommonFriends(Long firstUserId, Long secondUserId) {
        log.info("Вызов метода получения общих друзей двух пользователей, firstUserId = {}, secondUserId = {}", firstUserId, secondUserId);
        User user = users.get(firstUserId);
        User secondUser = users.get(secondUserId);
        if (user.getFriends() == null || secondUser.getFriends() == null) {
            return new ArrayList<>();
        } else {
            Set<Long> setFriendsUser1 = user.getFriends();
            Set<Long> setFriendsUser2 = secondUser.getFriends();
            Set<Long> setCommonFriend = new HashSet<>();
            setCommonFriend.clear();
            setCommonFriend.addAll(setFriendsUser1);
            setCommonFriend.retainAll(setFriendsUser2);
            List<User> listFriends = new ArrayList<>();
            for (Long friend : setCommonFriend) {
                listFriends.add(users.get(friend));
            }
            return listFriends;
        }
    }

    @Override
    public List<User> getFriends(Long userId) {
        log.info("Вызов метода получения списка друзей пользователя, userId = {}", userId);
        User user = users.get(userId);
        Set<Long> setFriends = user.getFriends();
        List<User> listFriends = new ArrayList<>();
        for (Long friend : setFriends) {
            listFriends.add(users.get(friend));
        }
        return listFriends;
    }
}
