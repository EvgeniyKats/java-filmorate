package ru.yandex.practicum.filmorate.storage.friend;

import ru.yandex.practicum.filmorate.model.UserFriendPair;

import java.util.List;

public interface FriendStorage {
    UserFriendPair getPairById(long pairId);

    List<UserFriendPair> getUserFriendsByUserId(long userId);
}
