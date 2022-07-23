package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Optional;

public interface GenreDao {
    //Поиск жанра по ID
    Optional<Genre> findGenreById(Integer id);
}
