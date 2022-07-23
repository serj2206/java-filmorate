package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.like.LikeDao;

@Slf4j
@Service
public class LikeService {
    private final UserService userService;
    private final FilmService filmService;
    private final LikeDao likeDao;

    public LikeService(UserService userService, FilmService filmService, LikeDao likeDao) {
        this.userService = userService;
        this.filmService = filmService;
        this.likeDao = likeDao;
    }


    //Добавление лайка от пользователя c id = idUser фильму c id = idFilm
    public boolean add(Long filmId, Long userId) {
        log.info(" LikeService.addLike(filmId = {}, userId = {})", filmId, userId);
        userService.findUserById(userId);
        filmService.findFilmById(filmId);
        return likeDao.add(filmId,userId);
    }

    //Удаление лайка от пользователя c id = idUser фильму c id = idFilm
    public boolean delete(Long filmId, Long userId) {
        log.info(" LikeService.deleteLike(filmId = {}, userId = {})", filmId, userId);
        userService.findUserById(userId);
        filmService.findFilmById(filmId);
        return likeDao.delete(filmId, userId);
    }
}
