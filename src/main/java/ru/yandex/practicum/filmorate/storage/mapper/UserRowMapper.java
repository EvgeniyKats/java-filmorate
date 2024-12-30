package ru.yandex.practicum.filmorate.storage.mapper;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friend.FriendStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Component
@AllArgsConstructor
public class UserRowMapper implements RowMapper<User> {
    private final FriendStorage friendStorage;

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong("user_id");
        String email = rs.getString("email_address");
        String login = rs.getString("login");
        String name = rs.getString("name");
        LocalDate birthday = convertDateToLocalDate(rs.getDate("birthday"));

        User result = User.builder()
                .id(id)
                .email(email)
                .login(login)
                .name(name)
                .birthday(birthday)
                .build();
        fillUserFriends(result);

        return result;
    }

    private LocalDate convertDateToLocalDate(Date date) {
        return LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    private void fillUserFriends(User user) {
        friendStorage.getUserFriendsByUserId(user.getId()).forEach(pair -> user.addFriend(pair.getFriendId()));
    }
}
