package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class UserFriendPair {
    private long id;
    private long userId;
    private long friendId;
}
