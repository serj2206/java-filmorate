package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;


@RestController
@Slf4j
public class UserController {

    private InMemoryUserStorage inMemoryUserStorage;

    public UserController() {

    }

    @Autowired
    public UserController(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }
    /*
    эндпоинт: создание пользователя;
    */
    @PostMapping("/users")
    public User create(@Validated @RequestBody User user) {
        log.debug("Получен POST-запрос на добавление нового пользователя.");
        return inMemoryUserStorage.add(user);
    }

    /*
    //эндпоинт: обновление пользователя;
    */
    @PutMapping("/users")
    public User update(@Valid @RequestBody User user) {
        log.debug("Получен PUT-запрос на обновление данных пользователя.");
        return inMemoryUserStorage.update(user);
    }

    /*
    //эндпоинт: получение списка всех пользователей.
    */
    @GetMapping("/users")
    public Collection<User> getList() {
        log.debug("Получен GET-запрос на получение списка пользователей.");
        return inMemoryUserStorage.getList();
    }


}



