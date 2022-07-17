package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;


public interface UserStorage {
    //Добавление пользователя
    public User add(User user);

    //Удаление пользователя
    public User delete(Long id);

    //Обновление пользователя
    public User update(User user);

    //Выгрузка списка пользователей
    public Collection<User> getList();

    //Выгрузка пользователя по id
    public User getUser(Long id);

    Optional<User> findUserByID(Long id);
}
