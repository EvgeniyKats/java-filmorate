package ru.yandex.practicum.filmorate.storage.friend;

import ru.yandex.practicum.filmorate.model.UserFriendPair;

import java.util.List;
import java.util.Optional;

public interface FriendStorage {
    Optional<UserFriendPair> getPairById(long pairId);

    List<UserFriendPair> getUserFriendsByUserId(long userId);

    void addFriend(long user, long friend);

    void removeFriend(long user, long friend);
}
