package ru.yandex.practicum.filmorate.storage.friend;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.UserFriendPair;

import java.util.List;

@Component
public class FriendDbStorage implements FriendStorage {
    @Override
    public UserFriendPair getPairById(long pairId) {
        return null;
    }

    @Override
    public List<UserFriendPair> getUserFriendsByUserId(long userId) {
        return List.of();
    }
}
