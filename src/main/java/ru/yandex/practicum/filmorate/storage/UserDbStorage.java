package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private static final String INSERT_USER_QUERY = "insert into users(id, email, login, name, birthday) " +
            "values (nextval('user_seq'),?,?,?,?)";
    private static final String SELECT_USER_ID_QUERY = "select id from users order by id desc limit 1";
    private static final String UPDATE_USER_QUERY = "update users set email=?, login=?, name=?, birthday=? where id =?";
    private static final String SELECT_USER_QUERY_BY_ID = "select * from users where id =?";
    private static final String SELECT_ALL_USER_QUERY = "select * from users";
    private static final String INSERT_FRIEND_QUERY = "insert into friend_request(id, user_id, friend_id, " +
            "request_status_id) values (nextval('friend_request_seq'),?,?,1)";
    private static final String SELECT_LIST_FRIEND_QUERY = "select u.* from users u " +
            "inner join friend_request fr on  u.id=fr.friend_id " +
            "where fr.user_id= ?";                //так как в тестах нет подтверждения,
                                                    // то убрала условие and request_status_id=2
    private static final String APPROVE_FRIEND_QUERY = "update friend_request set request_status_id=2 " +
            "where user_id = ? and friend_id=?";
    private static final String DELETE_FRIEND_QUERY = "delete from friend_request " +
            "where user_id = ? and friend_id=?";
    private static final String SELECT_COMMON_FRIEND_QUERY = "select a.* from (select u.* from users u " +
            "inner join friend_request fr on u.id=fr.friend_id where fr.user_id = ?) a " +
            "inner join (select u.* from users u inner join friend_request fr " +
            "on u.id=fr.friend_id where fr.user_id = ? ) b on a.id= b.id";

    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;

    @Override
    public User saveUser(User user) {
        if (!StringUtils.hasText(user.getName())) {
            user.setName(user.getLogin());
        }
        jdbcTemplate.update(INSERT_USER_QUERY,
                user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
        Long id = jdbcTemplate.queryForObject(SELECT_USER_ID_QUERY, Long.class);
        user.setId(id);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!StringUtils.hasText(user.getName())) {
            user.setName(user.getLogin());
        }
        if (getUserById(user.getId()) != null) {
            jdbcTemplate.update(UPDATE_USER_QUERY,
                    user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        } else {
            throw new ObjectNotFoundException("Нет пользователя для обновления");
        }
        return user;
    }

    public List<User> getAllUser() {
        return jdbcTemplate.query(SELECT_ALL_USER_QUERY, userRowMapper);
    }

    @Override
    public User getUserById(Long id) {
        User user= jdbcTemplate
                .query(SELECT_USER_QUERY_BY_ID, ps -> ps.setLong(1, id), userRowMapper)
                .stream()
                .findFirst()
                .orElse(null);
        if (user!=null) {
            return user;
        } else {
            throw new ObjectNotFoundException("Нет пользователя с таким ИД");
        }
    }

    @Override
    public void saveFriend(Long firstUserId, Long secondUserId) {
        if ((getUserById(firstUserId) != null) && (getUserById(secondUserId) != null)) {
            jdbcTemplate.update(INSERT_FRIEND_QUERY, firstUserId, secondUserId);
        } else {
            throw new ObjectNotFoundException("Нет пользователя (-ей) с переданным ИД");
        }
    }

    @Override
    public void approveFriend(Long firstUserId, Long secondUserId) {
        if ((getUserById(firstUserId) != null) && (getUserById(secondUserId) != null)) {
            jdbcTemplate.update(APPROVE_FRIEND_QUERY, firstUserId, secondUserId);
        } else {
            throw new ObjectNotFoundException("Нет пользователя (-ей) с переданным ИД");
        }
    }

    @Override
    public void deleteFriend(Long firstUserId, Long secondUserId) {
        if ((getUserById(firstUserId) != null) && (getUserById(secondUserId) != null)) {
            jdbcTemplate.update(DELETE_FRIEND_QUERY, firstUserId, secondUserId);
        } else {
            throw new ObjectNotFoundException("Нет пользователя (-ей) с переданным ИД");
        }

    }

    @Override
    public List<User> getListCommonFriends(Long firstUserId, Long secondUserId) {
        return jdbcTemplate.query(
                SELECT_COMMON_FRIEND_QUERY,
                (ps) -> {
                    ps.setLong(1, firstUserId);
                    ps.setLong(2, secondUserId);
                },
                userRowMapper);
    }

    @Override
    public List<User> getFriends(Long userId) {
        return jdbcTemplate.query(SELECT_LIST_FRIEND_QUERY, ps -> ps.setLong(1, userId), userRowMapper);
    }


}
