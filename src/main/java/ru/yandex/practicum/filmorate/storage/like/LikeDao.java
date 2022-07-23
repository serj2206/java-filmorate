package ru.yandex.practicum.filmorate.storage.like;

import java.util.Collection;

public interface LikeDao {
    //Добавить Like
    boolean add(Long film_id, Long user_id);

    //Удаление лайка
    boolean delete(Long film_id, Long user_id);

    //Популярные фильмы
    Collection<Long> getTop(Integer count);
}
