package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    public Film add(Film film);

    public Film delete(Integer id);

    public Film update(Film film);

    public Collection<Film> getFilms();

    public Film getFilm(Integer id);


}
