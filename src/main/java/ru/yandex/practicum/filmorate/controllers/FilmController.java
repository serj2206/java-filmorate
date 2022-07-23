package ru.yandex.practicum.filmorate.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.LikeService;
import java.util.*;

@RestController
@Slf4j
public class FilmController {

    private final FilmService filmService;
    private final LikeService likeService;

    @Autowired
    public FilmController(FilmService filmService, LikeService likeService) {
        this.filmService = filmService;
        this.likeService = likeService;
    }

    // эндпоинт: добавление фильма --Добро
    @PostMapping("/films")
    public Film create(@Validated @RequestBody Film film) {
        log.info("POST-запрос на добавление фильма.");
        return filmService.add(film);
    }

    // эндпоинт: обновление фильма
    @PutMapping("/films")
    public Film update(@Validated @RequestBody Film film) {
        log.info("PUT-запрос на обновление фильма.");
        return filmService.update(film);
    }

    //Пользователь ставит лайк фильму.
    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable Long id,
                        @PathVariable Long userId) {
        log.info("PUT-запрос на добавление пользователем лайка фильму.");
        if (id == null) throw new NullPointerException("Id = null");
        if (userId == null) throw new NullPointerException("userId = null");
        likeService.add(id, userId);
    }


    // эндпоинт: получение всех фильмов
    @GetMapping("/films")
    public Collection<Film> getList() {
        log.info("GET-запрос на предоставление списка фильмов.");
        return filmService.getFilms();
    }


    //Предоставление фильма по id --Добро
    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable Long id) {
        log.info("GET-запрос на предоставление фильма по id.");
        if (id == null) throw new NullPointerException("Id = null");
        return filmService.findFilmById(id);
    }

    //Возвращает список из первых count фильмов по количеству лайков.
    // Если значение параметра count не задано, верните первые 10.
    @GetMapping("/films/popular")
    public List<Film> getTopFilm(@RequestParam(defaultValue = "10") Integer count) {
        log.info("GET-запрос на предоставление популярных фильмов.");
        return filmService.getTop(count);
    }

    //Пользователь удаляет лайк.
    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable Long id,
                           @PathVariable Long userId) {
        log.info("DELETE-запрос на удаление лайка фильму.");
        if (id == null) throw new NullPointerException("Id = null");
        if (userId == null) throw new NullPointerException("userId = null");
        likeService.delete(id, userId);
    }

}
