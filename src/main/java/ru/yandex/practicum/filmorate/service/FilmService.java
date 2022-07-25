package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controllers.ValidationControl;
import ru.yandex.practicum.filmorate.exceptions.FilmNotDetectedException;
import ru.yandex.practicum.filmorate.exceptions.FilmNotUpdatedException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.filmgenre.FilmGenreDao;
import ru.yandex.practicum.filmorate.storage.like.LikeDao;

import java.util.*;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage inMemoryFilmStorage;

    private final ValidationControl validationControl;

    private final FilmGenreDao filmGenreDao;

    private final LikeDao likeDao;


    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") FilmStorage inMemoryFilmStorage
            , ValidationControl validationControl
            , FilmGenreDao filmGenreDao
            , LikeDao likeDao) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.validationControl = validationControl;
        this.filmGenreDao = filmGenreDao;
        this.likeDao = likeDao;
    }

    //Вернуть фильм по id
    public Film findFilmById(Long id) {
        log.debug(" FilmServis.findFilmById(id = {})", id);
        Optional<Film> optionalFilm = inMemoryFilmStorage.findFilmById(id);
        if (optionalFilm.isPresent()) {
            Film film = optionalFilm.get();
            film.setGenres(new HashSet<>(filmGenreDao.getFilmGenre(film.getId())));
            return film;
        } else throw new FilmNotDetectedException(String.format("Фильм c id= %s не обнаружен", id));
    }

    //Добавить фильм
    public Film add(Film film) {
        log.debug(" FilmService.add()");
        validationControl.createValidationFilm(film);
        Long id = inMemoryFilmStorage.add(film);
        filmGenreDao.add(film.getGenres(), id);
        return findFilmById(id);
    }

    //Обновить фильм
    public Film update(Film film) {
        log.debug(" FilmService.update()");
        validationControl.createValidationFilm(film);
        if (inMemoryFilmStorage.update(film)) {
            filmGenreDao.update(film);
            return findFilmById(film.getId());
        }
        throw new FilmNotUpdatedException(String.format("Ошибка обновления данных пользователя c id= %s", film.getId()));
    }

    //Список фильмов
    public Collection<Film> getFilms() {
        log.debug(" FilmService.update()");
        return inMemoryFilmStorage.getFilms();
    }

    //Предоставление списка состоящего из числа count наиболее популярных фильмов по количеству лайков
    public List<Film> getTop(int count) {
        log.debug(" FilmService.getTop(count = {})", count);
        List<Film> listFilm = new ArrayList<>();
        List<Long> listLike = new ArrayList<>(likeDao.getTop(count));
        if (listLike != null)
            if (!listLike.isEmpty()) {
                for (Long id : listLike) {
                    listFilm.add(findFilmById(id));
                }
            }
        return listFilm;
    }
}
