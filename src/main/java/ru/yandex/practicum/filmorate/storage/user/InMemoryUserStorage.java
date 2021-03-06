package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.controllers.ValidationControl;
import ru.yandex.practicum.filmorate.exceptions.UserNotDetectedException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private ValidationControl validationControl;
    private Map<Integer, User> listUser = new HashMap<>();
    int idGlobal = 0;
    public InMemoryUserStorage(){};

    @Autowired
    public InMemoryUserStorage(ValidationControl validationControl) {
        this.validationControl = validationControl;
    }

    //Добавление нового пользователя
    @Override
    public User add(@Validated User user) {
        log.debug("Получен POST-запрос на добавление нового пользователя.");
        validationControl.createValidationUser(user);
        setId();
        user.setId(idGlobal);
        listUser.put(idGlobal, user);
        log.debug("Пользователь с логином {} успешно добавлен. Ему присвоено ID = {}.", user.getLogin(), user.getId());
        return user;
    }

    //Удаление пользователя по iD
    @Override
    public User delete(Integer id) {
        User user;
        if (listUser.containsKey(id)) {
            user = listUser.get(id);
            listUser.remove(id);
            log.debug("Пользователь с login {} удален", user.getLogin());
        } else {
            user = null;
            log.debug("Пользователь с id {} не найден", id); //тут видимо нужно прерывание
        }
        return user;
    }

    //Обновление пользователя по ID
    @Override
    public User update(@Validated User user) {
        validationControl.updateValidationUser(user, listUser);
        listUser.put(user.getId(), user);
        log.debug("Данные пользователя с ID = {} успешно обновлены.", user.getId());
        return user;
    }

    //Ппредоставление списка пользователей
    @Override
    public Collection<User> getList() {
        return listUser.values();
    }

    //Предоставление данных пользователя по ID
    @Override
    public User getUser(Integer id) {
        if (listUser.containsKey(id)) {
            return listUser.get(id);
        }
        throw new UserNotDetectedException(String.format("Пользователь с id %s не найден", id));
    }

    //Итерация ID
    private void setId() {
        idGlobal++;
        log.debug("Счетчик ID пользователей увеличен на единицу.");
    }

    //Нахождение максимального ID
    private int getIdGlobal() {
        log.debug("Нахождение максимального ID пользователя.");
        int max = 0;
        if (listUser.isEmpty()) idGlobal = 0;
        else {
            for (int idUser : listUser.keySet()) {
                if (max < idUser) {
                    max = idUser;
                }
            }
        }
        idGlobal = max;
        log.debug("Максимальное ID пользователя: {}.", max);
        return idGlobal;
    }
}
