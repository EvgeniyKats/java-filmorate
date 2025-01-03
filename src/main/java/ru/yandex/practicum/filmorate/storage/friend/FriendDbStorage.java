package ru.yandex.practicum.filmorate.storage.friend;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.UserFriendPair;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class FriendDbStorage extends BaseRepository<UserFriendPair> implements FriendStorage {
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM user_friend WHERE id = ?;";
    private static final String FIND_USER_FRIENDS_QUERY = "SELECT * FROM user_friend WHERE user_id = ?;";
    private static final String INSERT_QUERY = "INSERT INTO user_friend (user_id, friend_id) VALUES (?, ?);";
    private static final String DELETE_QUERY = "DELETE FROM user_friend WHERE user_id = ? AND friend_id = ?;";

    public FriendDbStorage(JdbcTemplate jdbc, RowMapper<UserFriendPair> mapper) {
        super(jdbc, mapper);
    }


    @Override
    public Optional<UserFriendPair> getPairById(long pairId) {
        return findOne(FIND_BY_ID_QUERY, pairId);
    }

    @Override
    public List<UserFriendPair> getUserFriendsByUserId(long userId) {
        return findMany(FIND_USER_FRIENDS_QUERY, userId);
    }

    @Override
    public void addFriend(long user, long friend) {
        insert(INSERT_QUERY, Long.class, user, friend);
    }

    @Override
    public void removeFriend(long user, long friend) {
        delete(DELETE_QUERY, user, friend);
    }
}
