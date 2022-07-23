package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;

@Slf4j
@RestController
public class GenreController {

    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    //Список жанров
    @GetMapping("/genres")
    public Collection<Genre> getGenres() {
        log.info("Get-запрос список жанров");
        return genreService.getList();
    }

    //Жанр по id
    @GetMapping("/genres/{id}")
    public Genre findGenreById(@PathVariable Integer id) {
        log.info("Get-запрос жанра по id");
        return genreService.findGenreById(id);
    }

}
