package ru.yandex.practicum.filmorate.storage.filmgenre;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface FilmGenreDao {
    void add(Set<Genre> genres, Long filmId);

    //Обновление
    void update(Film film);

    //Выгрузка списка жанров принадлежащих фильму
    Collection<Genre> getFilmGenre(Long filmId);
}
