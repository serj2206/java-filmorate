package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotDetectedException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    UserStorage inMemoryUserStorage;

    public UserService() {
    }

    @Autowired
    public UserService(@Qualifier("UserDbStorage") UserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    //Добавить пользователя
    public User addUser(User user) {
        //Проверить валидацию
        inMemoryUserStorage.add(user);
        return user;
    }

    //Предоставить пользователя по Id
    public User findUserById (Long id) {
        inMemoryUserStorage.findUserByID(id);
        return null;
    }

    //Добавить пользователя fromUserId в друзья к пользователю toUserId
    public boolean addFriend (Long toUserId, Long fromUserId) {
        User fromUser = inMemoryUserStorage.getUser(fromUserId);
        User toUser = inMemoryUserStorage.getUser(toUserId);
        toUser.getFriends().add(fromUserId); //Добавляем в список друзей
        fromUser.getFriends().add(toUserId); //добавляем в список друзей
        return true;
    }

    //Удалить пользователя fromUserId из друзей пользователя toUserId
    public boolean deleteFriend (Long toUserId, Long fromUserId) {
        User fromUser = inMemoryUserStorage.getUser(fromUserId);
        User toUser = inMemoryUserStorage.getUser(toUserId);
        if (fromUser == null) {
            throw new UserNotDetectedException(String.format("Пользователь c id= %s не обнаружен", fromUserId));
        }
        if (toUser == null) {
            throw new UserNotDetectedException(String.format("Пользователь c id= %s не обнаружен", toUser));
        }
        toUser.getFriends().remove(fromUserId); //удаляем из списка друзей
        fromUser.getFriends().remove(toUserId); //Удаляем из списка друзей
        return true;
    }

    //Список друзей пользователя
    public Collection<User> getListFriends (Long id) {
        User user = inMemoryUserStorage.getUser(id);
        if (user == null) {
            throw new UserNotDetectedException(String.format("Пользователь c id= %s не обнаружен", id));
        }
        List<Long> listIdFriends = new ArrayList<>(inMemoryUserStorage.getUser(id).getFriends());
        List<User> listFriend = new ArrayList<>();

        for (Long idFriend : listIdFriends){
            if (inMemoryUserStorage.getUser(idFriend) != null) { //проверяем если id пользователя в списке друзей друга
                listFriend.add(inMemoryUserStorage.getUser(idFriend));
            }
        }
        return listFriend;
    }

    //Предоставление списка друзей являющихся общими с его друзьями
    public List<User> getCommonFriends (Long id, Long otherId) {
        User user = inMemoryUserStorage.getUser(id);
        User otherUser = inMemoryUserStorage.getUser(otherId);
        if (user == null) {
            throw new UserNotDetectedException(String.format("Пользователь c id= %s не обнаружен", id));
        }
        if (otherUser == null) {
            throw new UserNotDetectedException(String.format("Пользователь c id= %s не обнаружен", otherId));
        }
        List<Long> listFriends = new ArrayList<>(user.getFriends());
        List<Long> listOtherFriend =new ArrayList<>(otherUser.getFriends());
        List<User> listCommonFriends = new ArrayList<>();

        for (Long idFriend : listFriends) {
            if (listOtherFriend.contains(idFriend)) {
                listCommonFriends.add(inMemoryUserStorage.getUser(idFriend));
            }
        }
        return listCommonFriends;
    }

}
