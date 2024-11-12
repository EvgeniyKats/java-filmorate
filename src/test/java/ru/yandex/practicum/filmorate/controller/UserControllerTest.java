package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.DuplicateException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private UserController userController;

    @BeforeEach
    void beforeEach() {
        userController = new UserController();
    }

    @Test
    void shouldBeSuccessCreateUser() {
        User user = User.builder()
                .email("test@test.ru")
                .name("Name")
                .login("Login")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        User add = userController.createUser(user);
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
            userController.createUser(user);
            assertEquals(i + 1, userController.findAll().size());
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
        userController.createUser(user);

        User received = userController.findAll().iterator().next();
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
        userController.createUser(user);
        User received = userController.findAll().iterator().next();
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
        userController.createUser(user);
        User received = userController.findAll().iterator().next();
        assertEquals(received.getName(), received.getLogin());
    }

    @Test
    void shouldBeFailedCreateUserWithSpaceInLogin() {
        User user = User.builder()
                .email("test@test.ru")
                .name("Name")
                .login("Lo gin")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    void shouldBeFailedCreateUserWithBirthdayInFuture() {
        User user = User.builder()
                .email("test@test.ru")
                .name("Name")
                .login("Login")
                .birthday(LocalDate.now().plusDays(1))
                .build();
        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    void shouldBeFailedCreateUserAgain() {
        User user = User.builder()
                .email("test@test.ru")
                .name("Name")
                .login("Login")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        assertDoesNotThrow(() -> userController.createUser(user));
        assertThrows(DuplicateException.class, () -> userController.createUser(user));
    }

    @Test
    void shouldSuccessUpdateUser() {
        User user = User.builder()
                .email("test@test.ru")
                .name("Name")
                .login("Login")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        userController.createUser(user);
        User toUpdate = user.toBuilder()
                .login("NewLogin")
                .build();
        assertNotEquals(user.getLogin(), toUpdate.getLogin());
        userController.updateUser(toUpdate);

        User received = userController.findAll().iterator().next();
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
        assertThrows(ValidationException.class, () -> userController.updateUser(user));
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
        assertThrows(ValidationException.class, () -> userController.updateUser(user));
    }

    @Test
    void shouldSuccessUpdateUserWithNewEmail() {
        User user = User.builder()
                .email("test@test.ru")
                .name("Name")
                .login("Login")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        userController.createUser(user);
        User toUpdate = user.toBuilder()
                .email("new@test.ru")
                .build();
        assertDoesNotThrow(() -> userController.updateUser(toUpdate));
    }

    @Test
    void shouldFailedUpdateUserIfNewEmailAlreadyUsed() {
        User user = User.builder()
                .email("test@test.ru")
                .name("Name")
                .login("Login")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        user = userController.createUser(user);

        User anotherUser = User.builder()
                .email("new@test.ru")
                .name("Name")
                .login("Login")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        userController.createUser(anotherUser);

        User toUpdate = user.toBuilder()
                .email("new@test.ru")
                .build();
        assertThrows(DuplicateException.class, () -> userController.updateUser(toUpdate));
    }
}