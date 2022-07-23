package ru.yandex.practicum.filmorate.storage.filmgenre;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

public interface FilmGenreDao {
    void add(Film film, Long filmId);

    //Обновление
    void update(Film film);

    //Выгрузка списка жанров принадлежащих фильму
    Collection<Genre> getFilmGenre(Film film);
}
