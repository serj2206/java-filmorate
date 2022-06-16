package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotDetectedException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserService {
    UserStorage inMemoryUserStorage;

    public UserService() {
    }

    @Autowired
    public UserService(UserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    //Добавить пользователя fromUserId в друзья к пользователю toUserId
    public boolean addFriend (Integer fromUserId, Integer toUserId) {
        User fromUser = inMemoryUserStorage.getUser(fromUserId);
        User toUser = inMemoryUserStorage.getUser(toUserId);
        if (fromUser == null) {
            throw new UserNotDetectedException(String.format("Пользователь c id= %s не обнаружен", fromUserId));
        }
        if (toUser == null) {
            throw new UserNotDetectedException(String.format("Пользователь c id= %s не обнаружен", fromUserId));
        }
        toUser.addFriend(fromUserId);
        fromUser.addFriend(toUserId);
        return true;
    }

    //Удалить пользователя fromUserId из друзей пользователя toUserId
    public boolean deleteFriend (Integer fromUserId, Integer toUserId) {
        User fromUser = inMemoryUserStorage.getUser(fromUserId);
        User toUser = inMemoryUserStorage.getUser(toUserId);
        if (fromUser == null) {
            throw new UserNotDetectedException(String.format("Пользователь c id= %s не обнаружен", fromUserId));
        }
        if (toUser == null) {
            throw new UserNotDetectedException(String.format("Пользователь c id= %s не обнаружен", fromUserId));
        }
        toUser.deleteFriend(fromUserId);
        fromUser.deleteFriend(toUserId);
        return true;
    }

    //Список друзей пользователя
    public Collection<User> getListFriends (Integer id) {
        User user = inMemoryUserStorage.getUser(id);
        if (user == null) {
            throw new UserNotDetectedException(String.format("Пользователь c id= %s не обнаружен", id));
        }
        List<Integer> listIdFriends = inMemoryUserStorage.getUser(id).getFriends();
        List<User> listFriend = new ArrayList<>();
        for (Integer idFriend : listIdFriends){
            if (inMemoryUserStorage.getUser(idFriend) != null) {
                listFriend.add(inMemoryUserStorage.getUser(idFriend));
            }
        }
        return listFriend;
    }

    //Предоставление списка друзей являющихся общими с его друзьями
    public List<User> getCommonFriends (Integer id, Integer otherId) {
        User user = inMemoryUserStorage.getUser(id);
        User otherUser = inMemoryUserStorage.getUser(otherId);
        if (user == null) {
            throw new UserNotDetectedException(String.format("Пользователь c id= %s не обнаружен", id));
        }
        if (otherUser == null) {
            throw new UserNotDetectedException(String.format("Пользователь c id= %s не обнаружен", otherId));
        }
        List<Integer> listFriend = user.getFriends();
        List<Integer> listOtherFriend =otherUser.getFriends();
        List<User> listFriends = new ArrayList<>();

        for (Integer i : listFriend) {
            if (listOtherFriend.contains(i)) {
                listFriends.add(inMemoryUserStorage.getUser(i));
            }
        }
        return listFriends;
    }

}
