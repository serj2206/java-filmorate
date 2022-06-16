package ru.yandex.practicum.filmorate.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;

@RestController
@Slf4j
public class FilmController {
    private FilmStorage inMemoryFilmStorage;
    private FilmService filmService;
    public FilmController() {
    }
    @Autowired
    public FilmController(FilmStorage inMemoryFilmStorage, FilmService filmService) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.filmService = filmService;
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

    //Пользователь ставит лайк фильму.
    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable Integer id,
                        @PathVariable Integer userId) {
        filmService.addLike(id, userId);
    }

    //Пользователь удаляет лайк.
    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable Integer id,
                           @PathVariable Integer userId) {
        filmService.deleteLike(id, userId);
    }

    //Возвращает список из первых count фильмов по количеству лайков.
    // Если значение параметра count не задано, верните первые 10.
    @GetMapping("/films/popular")
    public List<Film> getTopFilm(@RequestParam(defaultValue = "10") Integer count) {
        return filmService.getTop(count);
    }
}
