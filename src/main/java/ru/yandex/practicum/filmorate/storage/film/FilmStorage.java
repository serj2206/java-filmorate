package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    //Добавить фильм в библиотеку
    public Film add(Film film);

    //Удалить фильм из библиотеки
    public Film delete(Long id);

    //Обновить фильм в библиотеке
    public Film update(Film film);

    //Предоставление списка фильмов
    public Collection<Film> getFilms();

    //Предоставление фильма по id
    public Film getFilm(Long id);


}
