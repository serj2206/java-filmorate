package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.util.*;

@RestController
@Slf4j
public class UserController {

    private final UserStorage inMemoryUserStorage;
    private final UserService userService;

    @Autowired
    public UserController(
            @Qualifier("UserDbStorage")
            UserStorage inMemoryUserStorage,
            UserService userService) {
        this.inMemoryUserStorage = inMemoryUserStorage;
        this.userService = userService;
    }

    //эндпоинт: создание пользователя;
    @PostMapping("/users")
    public User create(@Validated @RequestBody User user) {
        log.info("Получен POST-запрос на добавление нового пользователя.");
        return userService.addUser(user);
    }

    //эндпоинт: обновление пользователя;
    @PutMapping("/users")
    public User update(@Valid @RequestBody User user) {
        log.info("Получен PUT-запрос на обновление данных пользователя.");
        return inMemoryUserStorage.update(user);
    }

    //Добавление в друзья
    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id,
                          @PathVariable Long friendId) {
        log.info("Получен PUT запрос на добавления в друзья");
        if (id == null) throw new NullPointerException("Id = null");
        if (friendId == null) throw new NullPointerException("friendId = null");
        userService.addFriend(id, friendId);
    }

    //Предоставление пользователя по id
    @GetMapping("users/{id}")
    public User getUser(@PathVariable Long id) {
        log.info("Получен GET запрос на пердоставления данных пользователя по id");
        if (id == null) throw new NullPointerException("Id = null");
        return userService.findUserById(id);
    }

    //Предоставление списка друзей пользователя
    @GetMapping("/users/{id}/friends")
    public List<User> getListFriends(@PathVariable Long id) {
        log.info("Получен Put запрос на получение списка друзей");
        if (id == null) throw new NullPointerException("Id = null");
        return (List<User>) userService.getListFriends(id);
    }

    //эндпоинт: получение списка всех пользователей.
    @GetMapping("/users")
    public Collection<User> getList() {
        log.info("Получен GET-запрос на получение списка пользователей.");
        return inMemoryUserStorage.getList();
    }

    //Предоставление списка друзей являющихся общими с его друзьями
    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Long id,
                                       @PathVariable Long otherId) {
        if (id == null) throw new NullPointerException("Id = null");
        if (otherId == null) throw new NullPointerException("otherId = null");
        return userService.getCommonFriends(id, otherId);
    }

    //Удаление пользователя по id
    @DeleteMapping("/users/{id}")
    public User deleteUser(@PathVariable Long id){
        log.info("Получен DELETE-запрос на удаление пользователя по id.");
        if (id == null) throw new NullPointerException("Id = null");
        return inMemoryUserStorage.delete(id);
    }

    //Удаление из друзей
    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long id,
                             @PathVariable Long friendId) {
        log.info("Получен DELETE-запрос на удаление из друзей.");
        if (id == null) throw new NullPointerException("Id = null");
        if (friendId == null) throw new NullPointerException("otherId = null");
        userService.deleteFriend(id, friendId);
    }
}



