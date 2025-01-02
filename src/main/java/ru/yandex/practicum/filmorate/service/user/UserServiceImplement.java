package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.user.CreateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.exception.custom.DuplicateException;
import ru.yandex.practicum.filmorate.exception.custom.EntityNotExistException;
import ru.yandex.practicum.filmorate.exception.custom.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friend.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Slf4j
@Service
public class UserServiceImplement implements UserService {
    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    public UserServiceImplement(@Qualifier("userDbStorage") UserStorage userStorage, FriendStorage friendStorage) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
    }

    @Override
    public List<UserDto> findAll() {
        List<User> users = userStorage.getAllUsers();
        log.debug("Текущий список пользователей: {}.", users);
        return users.stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    @Override
    public UserDto createUser(CreateUserRequest request) {
        throwDuplicateIfEmailAlreadyInStorage(request.getEmail());
        User user = UserMapper.mapToUser(request);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.trace("Пользователь не указал имя. Для отображения используется логин.");
        }
        userStorage.addUser(user);
        log.info("User {}, был добавлен в хранилище.", user);
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public UserDto updateUser(UpdateUserRequest request) {
        User user = throwNotFoundIfIdAbsentInStorage(request.getId());
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            throwDuplicateIfEmailAlreadyInStorage(request.getEmail());
        }
        UserMapper.updateUserFields(user, request);
        user = userStorage.updateUser(user);
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public UserDto getUserById(long id) {
        User user = throwNotFoundIfIdAbsentInStorage(id);
        log.trace("Пользователь прошёл проверку на null, getUserById().");
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public List<UserDto> getFriends(Long userId) {
        User user = userStorage.getUserById(userId).orElseThrow(() -> new EntityNotExistException("User", userId));
        log.trace("Пользователь прошёл проверку на null, getFriends().");
        return user.getFriendsId().stream()
                .map(this::throwNotFoundIfIdAbsentInStorage)
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    @Override
    public List<Long> addFriend(Long userId, Long friendId) {
        User user = throwNotFoundIfIdAbsentInStorage(userId);
        throwNotFoundIfIdAbsentInStorage(friendId);
        log.trace("User, friend прошли проверку addFriend().");
        user.addFriend(friendId);
        friendStorage.addFriend(userId, friendId);
        log.trace("Успешное добавление в друзья.");
        return user.getFriendsId();
    }

    @Override
    public List<Long> removeFriend(Long userId, Long friendId) {
        User user = throwNotFoundIfIdAbsentInStorage(userId);
        throwNotFoundIfIdAbsentInStorage(friendId);
        log.trace("User, friend прошли проверку removeFriend().");
        user.removeFriend(friendId);
        friendStorage.removeFriend(userId, friendId);
        log.trace("Успешное удаление из друзей.");
        return user.getFriendsId();
    }

    @Override
    public List<UserDto> getCommonFriends(Long userId, Long friendId) {
        User user = throwNotFoundIfIdAbsentInStorage(userId);
        User friend = throwNotFoundIfIdAbsentInStorage(friendId);
        log.trace("User, friend прошли проверку getCommonFriends().");
        return userStorage.getCommonFriends(user, friend).stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    private User throwNotFoundIfIdAbsentInStorage(long id) {
        User user = userStorage.getUserById(id).orElseThrow(
                () -> new NotFoundException("Пользователь с id=" + id + " отсутствует в хранилище."));
        log.trace("User прошёл проверку на отсутствие id в хранилище.");
        return user;
    }

    private void throwDuplicateIfEmailAlreadyInStorage(String email) {
        if (userStorage.isEmailAlreadyInData(email)) {
            throw new DuplicateException("Такой Email уже используется " + email);
        }
        log.trace("User прошёл проверку на дубликат.");
    }
}
