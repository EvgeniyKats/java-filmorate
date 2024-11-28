package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> usersData;
    private final Set<User> usersSearch;

    public InMemoryUserStorage() {
        usersData = new HashMap<>();
        usersSearch = new HashSet<>();
    }

    @Override
    public User getUser(long id) {
        return usersData.get(id);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(usersData.values());
    }

    @Override
    public void addUser(User user) {
        long id = getNextId();
        log.debug("Был получен id для user: {}", id);
        user.setId(id);
        usersData.put(user.getId(), user);
        usersSearch.add(user);
    }

    @Override
    public Optional<User> updateUser(User newUser) {
        User oldUser = usersData.get(newUser.getId());
        if (oldUser == null) {
            log.info("User {}, отсутствует в хранилище и не был обновлен.", newUser.getId());
            return Optional.empty();
        }
        if (newUser.getEmail() != null) oldUser.setEmail(newUser.getEmail());
        if (newUser.getBirthday() != null) oldUser.setBirthday(newUser.getBirthday());
        if (newUser.getLogin() != null) oldUser.setLogin(newUser.getLogin());
        if (newUser.getName() != null && !newUser.getName().isBlank()) oldUser.setName(newUser.getName());
        log.info("User {}, был обновлен в хранилище.", oldUser);
        return Optional.of(oldUser);
    }

    @Override
    public void removeUser(long id) {
        usersData.remove(id);
    }

    @Override
    public boolean isUserInBaseByUser(User user) {
        return usersSearch.contains(user);
    }

    @Override
    public boolean isUserInBaseById(long id) {
        return usersData.containsKey(id);
    }

    private long getNextId() {
        long id = usersData.keySet().stream()
                .mapToLong(value -> value)
                .max()
                .orElse(0);
        return ++id;
    }
}
