package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.Optional;


public interface MpaDao {
    //public Integer add(Mpa mpa);

    //Предоставить список рейтингов возрастных ограничений
    Collection<Mpa> getList();

    //Предоставить данные Mpa по ID
    Optional<Mpa> findMpaById(Integer id);
}
