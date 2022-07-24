package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmNotDetectedException;
import ru.yandex.practicum.filmorate.exceptions.UserNotDetectedException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeDao;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashSet;
import java.util.Optional;

@Slf4j
@Service
public class LikeService {

    private final FilmStorage filmDbStorage;
    private final LikeDao likeDao;
    private final UserStorage userDbStorage;

    @Autowired
    public LikeService(FilmStorage filmDbStorage, LikeDao likeDao, UserStorage userDbStorage) {

        this.filmDbStorage = filmDbStorage;
        this.likeDao = likeDao;
        this.userDbStorage = userDbStorage;
    }


    //Добавление лайка от пользователя c id = idUser фильму c id = idFilm
    public boolean add(Long filmId, Long userId) {
        log.info(" LikeService.addLike(filmId = {}, userId = {})", filmId, userId);
        userDbStorage.findUserById(userId);
        filmDbStorage.findFilmById(filmId);
        return likeDao.add(filmId, userId);
    }

    //Удаление лайка от пользователя c id = idUser фильму c id = idFilm
    public boolean delete(Long filmId, Long userId) {
        log.info(" LikeService.deleteLike(filmId = {}, userId = {})", filmId, userId);
        findUserById(userId);
        filmDbStorage.findFilmById(filmId);
        return likeDao.delete(filmId, userId);
    }

    //Предоставить пользователя по Id
    public User findUserById(Long id) {
        log.info(" LikeService.findUserById(id = {})", id);
        Optional<User> userOptional = userDbStorage.findUserById(id);
        if (userOptional.isPresent())
            return userOptional.get();
        else throw new UserNotDetectedException(String.format("Пользователь c id= %s не обнаружен", id));
    }

    //Вернуть фильм по id
    public Film findFilmById(Long id) {
        log.info(" LikeService.findFilmById(id = {})", id);
        Optional<Film> optionalFilm = filmDbStorage.findFilmById(id);
        if (optionalFilm.isPresent()) {
            Film film = optionalFilm.get();
            return film;
        } else throw new FilmNotDetectedException(String.format("Фильм c id= %s не обнаружен", id));
    }


}
