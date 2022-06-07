package ru.yandex.practicum.filmorate.controllers;


import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@RestController
@Slf4j
public class FilmController {

    private Map<Integer, Film> collectionFilms = new HashMap<>();
    private int id;

    /*
    // эндпоинт: добавление фильма
    */
    @PostMapping("/films")
    public Film create(@Validated @RequestBody Film film) {
        log.debug("Получен POST-запрос на добавление фильма.");
        ValidationControl validationControl = new ValidationControl();
        validationControl.createValidationFilm(film);

        setId(); //Итерация id.
        film.setId(id); //Присвоение id фильму.
        collectionFilms.put(id, film); //Помещение фильма в коллекцию
        log.debug("Фильм с названием {} успешно добавлен. Присвоено ID = {}.", film.getName(), film.getId());
        return film;
    }

    /*
    // эндпоинт: обновление фильма
    */
    @PutMapping("/films")
    public Film update(@Validated @RequestBody Film film) {
        log.debug("Получен PUT-запрос на обновление фильма.");
        ValidationControl validationControl = new ValidationControl();
        validationControl.updateValidationFilm(film, collectionFilms);

        collectionFilms.put(film.getId(), film); //Обновление фильма в коллекции
        log.debug("Фильм с ID = {} успешно обновлен.", film.getId());
        return film;
    }

    /*
    // эндпоинт: получение всех фильмов
    */
    @GetMapping("/films")
    public Collection<Film> getList() {
        log.debug("Получен GET-запрос на предоставление списка фильмов.");
        return collectionFilms.values();
    }

    //Итерация ID
    private void setId() {
        id++;
        log.debug("Счетчик ID фильмов увеличен на единицу.");
    }

    //Нахождение максимального ID
    private int getId() {
        log.debug("Нахождение максимального ID фильма.");
        int max = 0;
        if (collectionFilms.isEmpty()) id = 0;
        else {
            for (int idFilm : collectionFilms.keySet()) {
                if (max < idFilm) {
                    max = idFilm;
                }
            }
        }
        id = max;
        log.debug("Максимальное ID фильма: {}.", max);
        return id;
    }
}
