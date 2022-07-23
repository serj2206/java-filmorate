package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controllers.ValidationControl;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friendship.FriendShipDao;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserStorage inMemoryUserStorage;
    private final ValidationControl validationControl;

    private final FriendShipDao friendShipDao;


    @Autowired
    public UserService(
            @Qualifier("UserDbStorage") UserStorage inMemoryUserStorage,
            ValidationControl validationControl,
            FriendShipDao friendShipDao) {
        this.inMemoryUserStorage = inMemoryUserStorage;
        this.validationControl = validationControl;
        this.friendShipDao = friendShipDao;
    }

    //Добавить пользователя
    public User addUser(User user) {
        validationControl.createValidationUser(user);
        return findUserById(inMemoryUserStorage.add(user));
    }

    //Предоставить пользователя по Id
    public User findUserById(Long id) {
        Optional<User> userOptional = inMemoryUserStorage.findUserByID(id);
        if (userOptional.isPresent())
            return userOptional.get();
        else throw new UserNotDetectedException(String.format("Пользователь c id= %s не обнаружен", id));
    }

    //Обновить пользователя
    public User update(User user) {
        validationControl.createValidationUser(user);
        if (inMemoryUserStorage.update(user)) {
            return findUserById(user.getId());
        }
        throw new UserNotUpdatedException(String.format("Ошибка обновления данных пользователя c id= %s", user.getId()));
    }

    //Получение списка все пользователей
    public Collection<User> getList() {
        return inMemoryUserStorage.getList();
    }

    //Удаление пользователя
    public User delete(Long id) {
        User user = findUserById(id);
        if (inMemoryUserStorage.delete(id)) return user;
        else throw new UserNotDeleteException(String.format("Ошибка удаления пользователя c id= %s", user.getId()));
    }

    //Найти друзей
    private boolean findFriend(Long userId, Long friendId) {
        return (friendShipDao.findFriend(userId, friendId));
    }

    //Добавить пользователя friendId в друзья к пользователю userId
    public boolean addFriend(Long userId, Long friendId) {
        findUserById(userId); //Проверить пользователя
        findUserById(friendId); //Проверить друга
        if (!findFriend(userId, friendId)) return friendShipDao.add(userId, friendId);
        else
            throw new ReplayFriendShipException("В списке друзей пользователя с id = {} пользователь с id = {} уже есть");
    }

    //Удалить пользователя fromUserId из друзей пользователя toUserId
    public boolean deleteFriend(Long userId, Long friendId) {
        findUserById(userId); //Проверить пользователя
        findUserById(friendId); //Проверить друга
        if (findFriend(userId, friendId)) return friendShipDao.delete(userId, friendId);
        else
            throw new FriendShipNotDeleteException ("В списке друзей пользователя с id = {} пользователь с id = {} отсутствует");
        }

    //Список друзей пользователя
    public Collection<User> getListFriends(Long id) {
        User user = findUserById(id);
        if (user == null) {
            throw new UserNotDetectedException(String.format("Пользователь c id= %s не обнаружен", id));
        }
        List<Long> listIdFriends = new ArrayList<>(friendShipDao.getList(id));
        List<User> listFriend = new ArrayList<>();

        for (Long idFriend : listIdFriends) {
            listFriend.add(findUserById(idFriend));
        }
        return listFriend;
    }

    //Предоставление списка друзей являющихся общими с его друзьями
    public List<User> getCommonFriends(Long id, Long otherId) {
        findUserById(id);
        findUserById(otherId);
        List<Long> listFriends = new ArrayList<>(friendShipDao.getCommonFriends(id, otherId));

        List<User> listCommonFriends = new ArrayList<>();

        for (Long idFriend : listFriends) {

                listCommonFriends.add(findUserById(idFriend));
            }
        return listCommonFriends;
    }

}
