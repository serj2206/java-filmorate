package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;


public interface UserStorage {
    //Добавление пользователя
    public Long add(User user);

    //Удаление пользователя
    public boolean delete(Long id);

    //Обновление пользователя
    public boolean update(User user);

    //Выгрузка списка пользователей
    public Collection<User> getList();

    //Выгрузка пользователя по id
    public User getUser(Long id);

    Optional<User> findUserById(Long id);
}
