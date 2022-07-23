package ru.yandex.practicum.filmorate.storage.friendship;

import java.util.Collection;

public interface FriendShipDao {
    //Найти друзей
    boolean findFriend(Long userId, Long friendId);

    //Добавить друга
    public boolean add(Long UserId, Long friendId);

    //Список друзей
    public Collection<Long> getList(Long userId);

    //Список общих друзей
    Collection<Long> getCommonFriends(Long id, Long otherId);

    //Удалить друга
    boolean delete(Long id, Long friendId);
}
