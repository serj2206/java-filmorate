package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;


public interface UserStorage {

    public User add(User user);

    public User delete(Integer id);

    public User update(User user);

    public Collection<User> getList();
}
