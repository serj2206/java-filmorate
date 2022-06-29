package ru.yandex.practicum.filmorate.exceptions;

public class FilmNotUpdatedException extends RuntimeException{
    public FilmNotUpdatedException (String message) {
        super(message);
    }
}
