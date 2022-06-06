package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Map;

@Slf4j
public class ValidationControl {

    public User createValidationUser (User user) {
        if (user == null) {
            throw new ValidationException("Тело запроса отсутствует!");
        }

        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("email отсутствует!");
        }

        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата Дня Рождения указана не верно!");
        }

        if (user.getLogin() == null || user.getLogin().isBlank()) {
            throw new ValidationException("login отсутствует.");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            log.debug("Имя пользователя не указано.");
            user.setName(user.getLogin());
        }
        return user;
    }

    public User updateValidationUser (User user, Map<Integer, User> listUser) {
        createValidationUser(user);

        if (!listUser.containsKey(user.getId())) {
            throw new ValidationException("ID пользователя не найдено!");
        }

        log.debug("ID пользователя {}.", user.getId());
        return user;
    }

    public Film createValidationFilm (Film film) {
        if (film == null) {
            throw new ValidationException("Тело запроса отсутствует!");
        }

        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Отсутствует название фильма!");
        }

        if (film.getDescription() != null && film.getDescription().length() > 200) {
            throw new ValidationException("Количество символов в описании превышает 200 символов!");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма задана отрицательным числом или равна нулю!");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата представления фильма стоит до 28 декабря 1895года!");
        }

        return film;
    }

    public Film updateValidationFilm (Film film, Map<Integer, Film> collectionFilms) {
        createValidationFilm(film);
        /*
        if (film == null) {
            throw new ValidationException("Тело запроса отсутствует!");
        }

         */
        if (!collectionFilms.containsKey(film.getId())) {
            throw new ValidationException("ID фильма не найдено!");
        }
/*
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Отсутствует название фильма!");
        }

        if (film.getDescription().length() > 200) {
            throw new ValidationException("Количество символов в описании превышает 200 символов!");
        }

        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма задана отрицательным числом или равна нулю!");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата представления фильма стоит до 28 декабря 1895года!");
        }

 */


        return film;
    }






}
