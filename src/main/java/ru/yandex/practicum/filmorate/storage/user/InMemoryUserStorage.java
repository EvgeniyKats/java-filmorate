package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> usersData;

    public InMemoryUserStorage() {
        usersData = new HashMap<>();
    }

    @Override
    public Optional<User> getUser(long id) {
        return Optional.ofNullable(usersData.get(id));
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(usersData.values());
    }

    @Override
    public boolean addUser(User user) {
        if (usersData.containsKey(user.getId())) return false;
        usersData.put(user.getId(), user);
        return true;
    }

    @Override
    public Optional<User> updateUser(User user) {
        if (!usersData.containsKey(user.getId())) return Optional.empty();
        return Optional.ofNullable(usersData.put(user.getId(), user));
    }

    @Override
    public void removeUser(long id) {
        usersData.remove(id);
    }
}
