package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository("userDbStorage")
public class UserDbStorage extends BaseRepository<User> implements UserStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM users;";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE user_id = ?;";
    private static final String FIND_BY_EMAIL_QUERY = "SELECT * FROM users WHERE email_address = ?;";
    private static final String INSERT_QUERY = "INSERT INTO users (email_address, login, name, birthday) " +
            "VALUES (?, ?, ?, ?);";
    private static final String UPDATE_QUERY = "UPDATE users SET email_address = ?, login = ?, name = ?, birthday = ? " +
            "WHERE user_id = ?;";
    private static final String DELETE_QUERY = "DELETE users WHERE user_id = ?;";
    private static final String FIND_COMMON_FRIENDS_QUERY = """
                    SELECT *
                    FROM users
                    WHERE user_id IN
                        (SELECT friend_id
                         FROM user_friend
                         WHERE user_id = ?
                           AND friend_id IN
                             (SELECT friend_id
                              FROM user_friend
                              WHERE user_id = ?));
            """;

    public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Optional<User> getUserById(long id) {
        Optional<User> result = findOne(FIND_BY_ID_QUERY, id);
        log.debug("getUserById, result = {}", result);
        return result;
    }

    @Override
    public boolean isEmailAlreadyInData(String email) {
        log.debug("isEmailAlreadyInData, email = {}", email);
        boolean result = findOne(FIND_BY_EMAIL_QUERY, email).isPresent();
        log.debug("isEmailAlreadyInData, email isPresent = {}", result);
        return result;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> result = findMany(FIND_ALL_QUERY);
        log.debug("getAllUsers, result = {}", result);
        return result;
    }

    @Override
    public User addUser(User user) {
        long id = insert(INSERT_QUERY, Long.class,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());
        user.setId(id);
        log.debug("addUser, user = {}", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        update(UPDATE_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        log.debug("updateUser, user = {}", user);
        return user;
    }

    @Override
    public void removeUser(long id) {
        log.debug("removeUser, id = {}", id);
        delete(DELETE_QUERY, id);
    }

    @Override
    public List<User> getCommonFriends(User user, User friend) {
        List<User> result = findMany(FIND_COMMON_FRIENDS_QUERY, user.getId(), friend.getId());
        log.debug("getCommonFriends, result = {}", result);
        return result;
    }
}
