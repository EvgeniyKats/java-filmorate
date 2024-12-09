package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.custom.DuplicateException;
import ru.yandex.practicum.filmorate.exception.custom.EntityNotExistException;
import ru.yandex.practicum.filmorate.exception.custom.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserServiceImplement;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplementTest {
    private UserService userService;

    @BeforeEach
    void beforeEach() {
        UserStorage storage = new InMemoryUserStorage();
        userService = new UserServiceImplement(storage);
    }

    @Test
    void shouldBeSuccessCreateUser() {
        User user = User.builder()
                .email("test@test.ru")
                .name("Name")
                .login("Login")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        User add = userService.createUser(user);
        assertNotNull(add.getId());
        assertEquals(user, add);
        assertEquals(user.getEmail(), add.getEmail());
        assertEquals(user.getName(), add.getName());
        assertEquals(user.getLogin(), add.getLogin());
        assertEquals(user.getBirthday(), add.getBirthday());
    }

    @Test
    void shouldBeSuccessCreate1000Users() {
        for (int i = 0; i < 1000; i++) {
            String email = "test" + i + "@test.ru";
            User user = User.builder()
                    .email(email)
                    .name("Name")
                    .login("Login")
                    .birthday(LocalDate.of(2000, 1, 1))
                    .build();
            userService.createUser(user);
            assertEquals(i + 1, userService.findAll().size());
        }
    }

    @Test
    void shouldBeSuccessGetCreatedUser() {
        User user = User.builder()
                .email("test@test.ru")
                .name("Name")
                .login("Login")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        userService.createUser(user);

        User received = userService.findAll().getFirst();
        assertNotNull(received.getId());
        assertEquals(user, received);
        assertEquals(user.getEmail(), received.getEmail());
        assertEquals(user.getName(), received.getName());
        assertEquals(user.getLogin(), received.getLogin());
        assertEquals(user.getBirthday(), received.getBirthday());
    }

    @Test
    void shouldBeSuccessCreateUserWithNullName() {
        User user = User.builder()
                .email("test@test.ru")
                .login("Login")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        userService.createUser(user);
        User received = userService.findAll().getFirst();
        assertEquals(received.getName(), received.getLogin());
    }

    @Test
    void shouldBeSuccessCreateUserWithEmptyName() {
        User user = User.builder()
                .email("test@test.ru")
                .name("   ")
                .login("Login")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        userService.createUser(user);
        User received = userService.findAll().getFirst();
        assertEquals(received.getName(), received.getLogin());
    }

    @Test
    void shouldBeFailedCreateUserAgain() {
        User user = User.builder()
                .email("test@test.ru")
                .name("Name")
                .login("Login")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        assertDoesNotThrow(() -> userService.createUser(user));
        assertThrows(DuplicateException.class, () -> userService.createUser(user));
    }

    @Test
    void shouldSuccessUpdateUser() {
        User user = User.builder()
                .email("test@test.ru")
                .name("Name")
                .login("Login")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        userService.createUser(user);
        User toUpdate = user.toBuilder()
                .login("NewLogin")
                .build();
        assertNotEquals(user.getLogin(), toUpdate.getLogin());
        userService.updateUser(toUpdate);

        User received = userService.findAll().getFirst();
        assertEquals(toUpdate.getLogin(), received.getLogin());
    }

    @Test
    void shouldFailedUpdateUserWithNullId() {
        User user = User.builder()
                .email("test@test.ru")
                .name("Name")
                .login("Login")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        assertThrows(NotFoundException.class, () -> userService.updateUser(user));
    }

    @Test
    void shouldFailedUpdateUserIfHolderNotContainsId() {
        User user = User.builder()
                .id(1L)
                .email("test@test.ru")
                .name("Name")
                .login("Login")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        assertThrows(NotFoundException.class, () -> userService.updateUser(user));
    }

    @Test
    void shouldSuccessUpdateUserWithNewEmail() {
        User user = User.builder()
                .email("test@test.ru")
                .name("Name")
                .login("Login")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        userService.createUser(user);
        User toUpdate = user.toBuilder()
                .email("new@test.ru")
                .build();
        assertDoesNotThrow(() -> userService.updateUser(toUpdate));
    }

    @Test
    void shouldFailedUpdateUserIfNewEmailAlreadyUsed() {
        User user = User.builder()
                .email("test@test.ru")
                .name("Name")
                .login("Login")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        user = userService.createUser(user);

        User anotherUser = User.builder()
                .email("new@test.ru")
                .name("Name")
                .login("Login")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        userService.createUser(anotherUser);

        User toUpdate = user.toBuilder()
                .email("new@test.ru")
                .build();
        assertThrows(DuplicateException.class, () -> userService.updateUser(toUpdate));
    }

    @Test
    void shouldBeZeroFriends() {
        User user = User.builder()
                .email("test@test.ru")
                .name("Name")
                .login("Login")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        user = userService.createUser(user);
        assertEquals(0, user.getFriendsId().size());
    }

    @Test
    void shouldBeAddOneFriend() {
        User user1 = User.builder()
                .email("test1@test.ru")
                .name("Name")
                .login("Login")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        user1 = userService.createUser(user1);

        User user2 = User.builder()
                .email("test2@test.ru")
                .name("Name")
                .login("Login")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        user2 = userService.createUser(user2);

        userService.addFriend(user1.getId(), user2.getId());

        assertEquals(1, user2.getFriendsId().size());
        assertEquals(1, user1.getFriendsId().size());

        List<User> all = userService.findAll();
        for (User user : all) {
            assertEquals(1, user.getFriendsId().size());
        }
    }

    @Test
    void shouldBeAddTwoFriends() {
        User user1 = User.builder()
                .email("test1@test.ru")
                .name("Name")
                .login("Login")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        user1 = userService.createUser(user1);

        User user2 = User.builder()
                .email("test2@test.ru")
                .name("Name")
                .login("Login")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        user2 = userService.createUser(user2);


        User user3 = User.builder()
                .email("test3@test.ru")
                .name("Name")
                .login("Login")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        user3 = userService.createUser(user3);

        userService.addFriend(user1.getId(), user2.getId());
        userService.addFriend(user1.getId(), user3.getId());
        userService.addFriend(user2.getId(), user3.getId());


        assertEquals(2, user1.getFriendsId().size());
        assertEquals(2, user2.getFriendsId().size());
        assertEquals(2, user3.getFriendsId().size());

        List<User> all = userService.findAll();
        for (User user : all) {
            assertEquals(2, user.getFriendsId().size());
        }
    }

    @Test
    void shouldBeZeroCommonFriends() {
        User user1 = User.builder()
                .email("test1@test.ru")
                .name("Name")
                .login("Login")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        user1 = userService.createUser(user1);

        User user2 = User.builder()
                .email("test2@test.ru")
                .name("Name")
                .login("Login")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        user2 = userService.createUser(user2);


        User user3 = User.builder()
                .email("test3@test.ru")
                .name("Name")
                .login("Login")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        user3 = userService.createUser(user3);

        userService.addFriend(user1.getId(), user2.getId());

        List<User> commonFriends = userService.getCommonFriends(user1.getId(), user3.getId());
        assertEquals(0, commonFriends.size());
    }

    @Test
    void shouldBeOneCommonFriends() {
        User user1 = User.builder()
                .email("test1@test.ru")
                .name("Name")
                .login("Login")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        user1 = userService.createUser(user1);

        User user2 = User.builder()
                .email("test2@test.ru")
                .name("Name")
                .login("Login")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        user2 = userService.createUser(user2);


        User user3 = User.builder()
                .email("test3@test.ru")
                .name("Name")
                .login("Login")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        user3 = userService.createUser(user3);

        userService.addFriend(user1.getId(), user2.getId());
        userService.addFriend(user3.getId(), user2.getId());

        List<User> commonFriends = userService.getCommonFriends(user1.getId(), user3.getId());
        assertEquals(1, commonFriends.size());
        assertEquals(user2.getId(), commonFriends.getFirst().getId());
    }

    @Test
    void shouldBeFailedAddFriendInFriendNotAdd() {
        User user1 = User.builder()
                .email("test1@test.ru")
                .name("Name")
                .login("Login")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        user1 = userService.createUser(user1);

        User user2 = User.builder()
                .id(2L)
                .email("test2@test.ru")
                .name("Name")
                .login("Login")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        User finalUser = user1;
        assertThrows(EntityNotExistException.class, () -> userService.addFriend(finalUser.getId(), user2.getId()));
        assertEquals(0, user1.getFriendsId().size());
        assertEquals(0, user2.getFriendsId().size());
    }

    @Test
    void shouldBeFailedAddFriendIfUserNotAdd() {
        User user1 = User.builder()
                .id(2L)
                .email("test1@test.ru")
                .name("Name")
                .login("Login")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        User user2 = User.builder()
                .email("test2@test.ru")
                .name("Name")
                .login("Login")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        user2 = userService.createUser(user2);

        User finalUser2 = user2;
        assertThrows(EntityNotExistException.class, () -> userService.addFriend(user1.getId(), finalUser2.getId()));
        assertEquals(0, user1.getFriendsId().size());
        assertEquals(0, user2.getFriendsId().size());
    }

    @Test
    void shouldBeFailedRemoveFriendInFriendNotAdd() {
        User user1 = User.builder()
                .email("test1@test.ru")
                .name("Name")
                .login("Login")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        user1 = userService.createUser(user1);

        User user2 = User.builder()
                .email("test2@test.ru")
                .name("Name")
                .login("Login")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        user2 = userService.createUser(user2);


        User user3 = User.builder()
                .id(9L)
                .email("test3@test.ru")
                .name("Name")
                .login("Login")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        userService.addFriend(user1.getId(), user2.getId());

        User finalUser = user1;
        assertThrows(EntityNotExistException.class, () -> userService.removeFriend(finalUser.getId(), user3.getId()));
        assertThrows(EntityNotExistException.class, () -> userService.removeFriend(user3.getId(), finalUser.getId()));

        assertEquals(1, user1.getFriendsId().size());
        assertEquals(1, user2.getFriendsId().size());
        assertEquals(0, user3.getFriendsId().size());

        List<User> all = userService.findAll();
        for (User user : all) {
            assertEquals(1, user.getFriendsId().size());
        }
    }

    @Test
    void shouldBeRemoveOneFriend() {
        User user1 = User.builder()
                .email("test1@test.ru")
                .name("Name")
                .login("Login")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        user1 = userService.createUser(user1);

        User user2 = User.builder()
                .email("test2@test.ru")
                .name("Name")
                .login("Login")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        user2 = userService.createUser(user2);
        userService.addFriend(user1.getId(), user2.getId());
        userService.removeFriend(user1.getId(), user2.getId());
        for (User user : userService.findAll()) {
            assertEquals(0, user.getFriendsId().size());
        }
        userService.addFriend(user1.getId(), user2.getId());
        userService.removeFriend(user2.getId(), user1.getId());
        for (User user : userService.findAll()) {
            assertEquals(0, user.getFriendsId().size());
        }
    }
}