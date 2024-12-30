package ru.yandex.practicum.filmorate.storage.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.UserFriendPair;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FriendsRowMapper implements RowMapper<UserFriendPair> {
    @Override
    public UserFriendPair mapRow(ResultSet rs, int numberRow) throws SQLException {
        UserFriendPair result = new UserFriendPair();
        result.setId(rs.getLong("id"));
        result.setUserId(rs.getLong("user_id"));
        result.setFriendId(rs.getLong("friend_id"));
        return result;
    }
}
