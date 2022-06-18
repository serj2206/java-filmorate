package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controllers.ValidationControl;
import ru.yandex.practicum.filmorate.exceptions.FilmNotDetectedException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private Map<Integer, Film> collectionFilms = new HashMap<>();
    private int idGlobal;
    private ValidationControl validationControl;

    @Autowired
    public InMemoryFilmStorage(ValidationControl validationControl) {
        this.validationControl = validationControl;
    }

    //Добавить фильм в библиотеку
    @Override
    public Film add(Film film) {
        validationControl.createValidationFilm(film);
        setId(); //Итерация id.
        film.setId(idGlobal); //Присвоение id фильму.
        collectionFilms.put(idGlobal, film); //Помещение фильма в коллекцию
        log.debug("Фильм с названием {} успешно добавлен. Присвоено ID = {}.", film.getName(), film.getId());
        return film;
    }

    //Удалить фильм из библиотеки
    public Film delete(Integer id) {
        Film film;
        if (collectionFilms.containsKey(id)) {
            film = collectionFilms.get(id);
            collectionFilms.remove(id);
            log.debug("Фильм с названием {} удален", film.getName());
        } else {
            film = null;
            log.debug("Фильм с id {} не найден", id); //тут видимо нужно прерывание
        }
        return film;
    }

    //Обновить фильм в библиотеке
    public Film update(Film film) {
        validationControl.updateValidationFilm(film, collectionFilms);
        collectionFilms.put(film.getId(), film); //Обновление фильма в коллекции
        log.debug("Фильм с ID = {} успешно обновлен.", film.getId());
        return film;
    }

    //Предоставление списка фильмов
    @Override
    public Collection<Film> getFilms() {
        return collectionFilms.values();
    }

    //Предоставление фильма по id
    @Override
    public Film getFilm(Integer id) {
        if (collectionFilms.containsKey(id)) {
            return collectionFilms.get(id);
        }
        throw new FilmNotDetectedException(String.format("Фильм с id %s не найден", id));
    }


    //Итерация ID
    private void setId() {
        idGlobal++;
        log.debug("Счетчик ID фильмов увеличен на единицу.");
    }

    //Нахождение максимального ID
    private int getIdGlobal() {
        log.debug("Нахождение максимального ID фильма.");
        int max = 0;
        if (collectionFilms.isEmpty()) idGlobal = 0;
        else {
            for (int idFilm : collectionFilms.keySet()) {
                if (max < idFilm) {
                    max = idFilm;
                }
            }
        }
        idGlobal = max;
        log.debug("Максимальное ID фильма: {}.", max);
        return idGlobal;
    }
}
