package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;


@RestController
@Slf4j
public class UserController {

//    private final static Logger log = LoggerFactory.getLogger(UserController.class);

    private Map<Integer, User> listUser = new HashMap<>();
    int id = 0;

    /*
    эндпоинт: создание пользователя;
    */
    @PostMapping("/users")
    public User create(@Validated @RequestBody User user) {
        log.debug("Получен POST-запрос на добавление нового пользователя.");

        if (user == null) {

            throw new ValidationException("Тело запроса отсутствует.");
        }

        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("email отсутствует.");
        }

        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата Дня Рождения указана не верно!");
        }

        if (user.getLogin() == null || user.getLogin().isBlank()) {
            throw new ValidationException("login отсутствует.");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            log.debug("Имя пользователя не указано.");
            user.setName(user.getLogin());
        }
        setId();
        user.setId(id);
        listUser.put(id, user);
        log.debug("Пользователь с логином {} успешно добавлен. Ему присвоено ID = {}.", user.getLogin(), user.getId());
        return user;
    }

    /*
    //эндпоинт: обновление пользователя;
    */
    @PutMapping("/users")
    public User update(@Valid @RequestBody User user) {
        log.debug("Получен PUT-запрос на обновление данных пользователя.");
//        try {
        if (user == null) {
            throw new ValidationException("Тело запроса отсутствует!");
        }
        if (!listUser.containsKey(user.getId())) {
            throw new ValidationException("ID пользователя не найдено!");
        }


        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("email отсутствует!");
        }


        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата Дня Рождения указана не верно!");
        }

        log.debug("ID пользователя {}.", user.getId());
        User userTemp = listUser.get(user.getId());


        if (user.getLogin() == null || user.getLogin().isBlank()) {
            throw new ValidationException("login отсутствует.");
        }


        if (user.getName() == null || user.getName().isBlank()) {
            log.debug("Имя пользователя не указано.");
            user.setName(user.getLogin());
        }

        listUser.put(user.getId(), user);
        log.debug("Данные пользователя с ID = {} успешно обновлены.", user.getId());
        return user;
    }

    /*
    //эндпоинт: получение списка всех пользователей.
    */
    @GetMapping("/users")
    public Collection<User> getList() {
        log.debug("Получен GET-запрос на получение списка пользователей.");
        return listUser.values();
    }

    //Итерация ID
    private void setId() {
        id++;
        log.debug("Счетчик ID пользователей увеличен на единицу.");
    }

    //Нахождение максимального ID
    private int getId() {
        log.debug("Нахождение максимального ID пользователя.");
        int max = 0;
        if (listUser.isEmpty()) id = 0;
        else {
            for (int idUser : listUser.keySet()) {
                if (max < idUser) {
                    max = idUser;
                }
            }
        }
        id = max;
        log.debug("Максимальное ID пользователя: {}.", max);
        return id;
    }
}



