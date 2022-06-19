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
        log.info("Получен POST-запрос на добавление фильма.");
        return inMemoryFilmStorage.add(film);
    }

    // эндпоинт: обновление фильма
    @PutMapping("/films")
    public Film update(@Validated @RequestBody Film film) {
        log.info("Получен PUT-запрос на обновление фильма.");
        return inMemoryFilmStorage.update(film);
    }

    //Пользователь ставит лайк фильму.
    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable Integer id,
                        @PathVariable Integer userId) {
        if (id == null) throw new NullPointerException("Id = null");
        if (userId == null) throw new NullPointerException("userId = null");
        filmService.addLike(id, userId);
    }

    // эндпоинт: получение всех фильмов
    @GetMapping("/films")
    public Collection<Film> getList() {
        log.info("Получен GET-запрос на предоставление списка фильмов.");
        return inMemoryFilmStorage.getFilms();
    }

    //Предоставление фильма по id
    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable Integer id) {
        log.info("Получен GET-запрос на предоставление списка фильмов.");
        if (id == null) throw new NullPointerException("Id = null");
        return inMemoryFilmStorage.getFilm(id);
    }

    //Возвращает список из первых count фильмов по количеству лайков.
    // Если значение параметра count не задано, верните первые 10.
    @GetMapping("/films/popular")
    public List<Film> getTopFilm(@RequestParam(defaultValue = "10") Integer count) {
        return filmService.getTop(count);
    }

    //Пользователь удаляет лайк.
    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable Integer id,
                           @PathVariable Integer userId) {
        if (id == null) throw new NullPointerException("Id = null");
        if (userId == null) throw new NullPointerException("userId = null");
        filmService.deleteLike(id, userId);
    }
}
