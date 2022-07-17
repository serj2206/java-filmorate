package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmNotDetectedException;
import ru.yandex.practicum.filmorate.exceptions.UserNotDetectedException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    FilmStorage inMemoryFilmStorage;
    UserStorage inMemoryUserStorage;

    public FilmService() {
    }

    @Autowired
    public FilmService(FilmStorage inMemoryFilmStorage, UserStorage inMemoryUserStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    //Добавление лайка от пользователя c id = idUser фильму c id = idFilm
    public boolean addLike(Long idFilm, Long idUser) {
        if (inMemoryUserStorage.getUser(idUser) == null) {
            throw new UserNotDetectedException(String.format("Пользователь c id= %s не обнаружен", idUser));
        }
        if (inMemoryFilmStorage.getFilm(idFilm) == null) {
            throw new FilmNotDetectedException(String.format("Фильм c id= %s не обнаружен", idFilm));
        }
        Film film = inMemoryFilmStorage.getFilm(idFilm);
        film.getLikes().add(idUser); //добавление лайка
        return true;
    }

    //Удаление лайка от пользователя c id = idUser фильму c id = idFilm
    public boolean deleteLike(Long idFilm, Long idUser) {
        if (inMemoryUserStorage.getUser(idUser) == null) {
            throw new UserNotDetectedException(String.format("Пользователь c id= %s не обнаружен", idUser));
        }
        if (inMemoryFilmStorage.getFilm(idFilm) == null) {
            throw new FilmNotDetectedException(String.format("Фильм c id= %s не обнаружен", idFilm));
        }
        Film film = inMemoryFilmStorage.getFilm(idFilm);
        film.getLikes().remove(idUser); //Удаление лайка
        return true;
    }

    //Предоставление списка состоящего из числа count наиболее популярных фильмов по количеству лайков
    public List<Film> getTop(int count) {

        return inMemoryFilmStorage.getFilms().stream()
                .sorted((Film film1, Film film2) -> {
                        if (film1.getLikes().size() > film2.getLikes().size()) return -1;
                        if (film1.getLikes().size() < film2.getLikes().size()) return 1;
                        return 0;
                })
                .limit(count)
                .collect(Collectors.toList());
    }

}
