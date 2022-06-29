package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
public class UserService {

    private final UserStorage userDbStorage;
    private final ValidationControl validationControl;

    private final FriendShipDao friendShipDao;

    @Autowired
    public UserService(
            @Qualifier("UserDbStorage") UserStorage inMemoryUserStorage,
            ValidationControl validationControl,
            FriendShipDao friendShipDao) {
        this.userDbStorage = inMemoryUserStorage;
        this.validationControl = validationControl;
        this.friendShipDao = friendShipDao;
    }

    //Добавить пользователя
    public User addUser(User user) {
        log.debug(" UserService.addUser()");
        validationControl.createValidationUser(user);
        return findUserById(userDbStorage.add(user));
    }

    //Предоставить пользователя по Id
    public User findUserById(Long id) {
        log.debug(" UserService.findUserById(id = {})", id);
        Optional<User> userOptional = userDbStorage.findUserById(id);
        if (userOptional.isPresent())
            return userOptional.get();
        else throw new UserNotDetectedException(String.format("Пользователь c id= %s не обнаружен", id));
    }

    //Обновить пользователя
    public User update(User user) {
        log.debug(" UserService.update()");
        validationControl.createValidationUser(user);
        if (userDbStorage.update(user)) {
            return findUserById(user.getId());
        }
        throw new UserNotUpdatedException(String.format("Ошибка обновления данных пользователя c id= %s", user.getId()));
    }

    //Получение списка все пользователей
    public Collection<User> getList() {
        log.debug(" UserService.getList()");
        return userDbStorage.getList();
    }

    //Удаление пользователя
    public User delete(Long id) {
        log.debug(" UserService.delete(id = {})", id);
        User user = findUserById(id);
        if (userDbStorage.delete(id)) return user;
        else throw new UserNotDeleteException(String.format("Ошибка удаления пользователя c id= %s", user.getId()));
    }

    //Найти друзей
    private boolean findFriend(Long userId, Long friendId) {
        log.debug(" UserService.findFriend(userId = {}, friendId = {})", userId, friendId);
        return (friendShipDao.findFriend(userId, friendId));
    }

    //Добавить пользователя friendId в друзья к пользователю userId
    public boolean addFriend(Long userId, Long friendId) {
        log.debug(" UserService.addFriend(userId = {}, friendId = {})", userId, friendId);
        findUserById(userId); //Проверить пользователя
        findUserById(friendId); //Проверить друга
        if (!findFriend(userId, friendId)) return friendShipDao.add(userId, friendId);
        else
            throw new ReplayFriendShipException("В списке друзей пользователя с id = {} пользователь с id = {} уже есть");
    }

    //Удалить пользователя fromUserId из друзей пользователя toUserId
    public boolean deleteFriend(Long userId, Long friendId) {
        log.debug(" UserService.deleteFriend(userId = {}, friendId = {})", userId, friendId);
        findUserById(userId); //Проверить пользователя
        findUserById(friendId); //Проверить друга
        if (findFriend(userId, friendId)) return friendShipDao.delete(userId, friendId);
        else
            throw new FriendShipNotDeleteException ("В списке друзей пользователя с id = {} пользователь с id = {} отсутствует");
        }

    //Список друзей пользователя
    public Collection<User> getListFriends(Long id) {
        log.debug(" UserService.getListFriends(id = {})", id);
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
        log.debug(" UserService.getCommonFriends(id = {}, otherId = {})", id, otherId);
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
