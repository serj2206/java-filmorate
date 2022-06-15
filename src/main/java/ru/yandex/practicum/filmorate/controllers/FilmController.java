package ru.yandex.practicum.filmorate.controllers;


import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.*;

@RestController
@Slf4j
public class FilmController {
    private InMemoryFilmStorage inMemoryFilmStorage;
    public FilmController() {
    }
    @Autowired
    public FilmController(InMemoryFilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    // эндпоинт: добавление фильма
    @PostMapping("/films")
    public Film create(@Validated @RequestBody Film film) {
        log.debug("Получен POST-запрос на добавление фильма.");
        return inMemoryFilmStorage.add(film);
    }

    // эндпоинт: обновление фильма
    @PutMapping("/films")
    public Film update(@Validated @RequestBody Film film) {
        log.debug("Получен PUT-запрос на обновление фильма.");
        return inMemoryFilmStorage.update(film);
    }

    // эндпоинт: получение всех фильмов
    @GetMapping("/films")
    public Collection<Film> getList() {
        log.debug("Получен GET-запрос на предоставление списка фильмов.");
        return inMemoryFilmStorage.getFilms();
    }
}
