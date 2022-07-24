package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    //Вернуть фильм по id
    Optional<Film> findFilmById(Long Id);

    //Добавить фильм в библиотеку
    public Long add(Film film);

    //Удалить фильм из библиотеки
    public boolean delete(Long id);

    //Обновить фильм в библиотеке
    public boolean update(Film film);

    //Предоставление списка фильмов
    public Collection<Film> getFilms();



}
