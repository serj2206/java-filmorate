package ru.yandex.practicum.filmorate.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.GenreNotDetectedException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDao;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
public class GenreService {
    private final GenreDao genreDao;

    @Autowired
    public GenreService(GenreDao genreDao) {
        this.genreDao = genreDao;
    }


    //Список жанров
    public Collection<Genre> getList() {
        return genreDao.getList();
    }

    //Жанр по id
    public Genre findGenreById(Integer id) {
        Optional<Genre> optionalGenre = genreDao.findGenreById(id);
        if (optionalGenre.isPresent()) {
            return optionalGenre.get();
        }
        else throw new GenreNotDetectedException(String.format("Жанр с ID = %s не обнаружен", id));
    }
}
