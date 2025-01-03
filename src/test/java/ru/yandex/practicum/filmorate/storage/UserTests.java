package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserFriendPair;
import ru.yandex.practicum.filmorate.storage.friend.FriendDbStorage;
import ru.yandex.practicum.filmorate.storage.mapper.FriendRowMapper;
import ru.yandex.practicum.filmorate.storage.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Import({UserDbStorage.class, UserRowMapper.class, FriendDbStorage.class, FriendRowMapper.class})
class UserTests {
    private final UserDbStorage userStorage;
    private final FriendDbStorage friendStorage;

    @Test
    void shouldBeZeroUser() {
        Optional<User> userOptional = userStorage.getUserById(1);
        assertThat(userOptional).isEmpty();
    }

    @Test
    void shouldGetUserById() {
        User u = addUser();
        Optional<User> userOptional = userStorage.getUserById(1);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", u.getId())
                );
    }

    @Test
    void shouldGetAllUsers() {
        User u = addUser();
        User u2 = addUser();
        assertThat(userStorage.getAllUsers()).size().isEqualTo(2);
        Optional<User> userOptional1 = userStorage.getUserById(1);
        Optional<User> userOptional2 = userStorage.getUserById(2);

        assertThat(userOptional1)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", u.getId())
                );

        assertThat(userOptional2)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", u2.getId())
                );
    }

    @Test
    void checkEmail() {
        User temp = addUser();
        long id = temp.getId() + 1;
        assertThat(userStorage.isEmailAlreadyInData("test" + id + "@test.ru")).isFalse();
        User u = addUser();
        Optional<User> userOptional1 = userStorage.getUserById(id);
        assertThat(userOptional1)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("email", u.getEmail())
                );
        assertThat(userStorage.isEmailAlreadyInData("test" + id + "@test.ru")).isTrue();
    }

    @Test
    void shouldRemoveUser() {
        User u = addUser();
        userStorage.removeUser(u.getId());
        Optional<User> userOptional1 = userStorage.getUserById(u.getId());
        assertThat(userOptional1).isEmpty();
    }

    @Test
    void shouldUpdateUser() {
        User u = addUser();
        User upd = u.toBuilder().build();
        upd.setName("new");
        userStorage.updateUser(upd);
        Optional<User> userOptional1 = userStorage.getUserById(u.getId());
        assertThat(userOptional1)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", upd.getName())
                );
    }

    @Test
    void shouldAddFriend() {
        User u1 = addUser();
        User u2 = addUser();
        friendStorage.addFriend(u1.getId(), u2.getId());
        assertThat(friendStorage.getPairById(1))
                .isPresent()
                .hasValueSatisfying(pair -> {
                    assertThat(pair).hasFieldOrPropertyWithValue("userId", u1.getId());
                    assertThat(pair).hasFieldOrPropertyWithValue("friendId", u2.getId());
                });
    }

    @Test
    void shouldGetUserFriendsByUserId() {
        User u1 = addUser();
        User u2 = addUser();
        User u3 = addUser();
        friendStorage.addFriend(u1.getId(), u2.getId());
        friendStorage.addFriend(u1.getId(), u3.getId());

        UserFriendPair p1 = friendStorage.getPairById(1).orElseThrow(RuntimeException::new);
        UserFriendPair p2 = friendStorage.getPairById(2).orElseThrow(RuntimeException::new);

        assertThat(friendStorage.getUserFriendsByUserId(u1.getId()))
                .contains(p1)
                .contains(p2);
    }

    @Test
    void shouldGetCommonFriends() {
        User u1 = addUser();
        User u2 = addUser();
        User u3 = addUser();
        assertThat(userStorage.getCommonFriends(u1, u2)).isEqualTo(List.of());
        friendStorage.addFriend(u1.getId(), u3.getId());
        friendStorage.addFriend(u2.getId(), u3.getId());
        assertThat(userStorage.getCommonFriends(u1, u2)).isEqualTo(List.of(u3));
    }

    @Test
    void shouldRemoveFriend() {
        User u1 = addUser();
        User u2 = addUser();
        friendStorage.addFriend(u1.getId(), u2.getId());
        friendStorage.removeFriend(u1.getId(), u2.getId());
        assertThat(friendStorage.getPairById(1)).isEmpty();
    }

    private User addUser() {
        long id = getIdNext();
        return userStorage.addUser(User.builder()
                .email("test" + id + "@test.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.now().minusDays(1))
                .build());
    }

    private long getIdNext() {
        long id = 1;
        while (userStorage.getUserById(id).isPresent()) {
            id++;
        }
        return id;
    }
}