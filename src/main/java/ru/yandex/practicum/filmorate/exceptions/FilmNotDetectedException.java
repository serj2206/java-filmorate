package ru.yandex.practicum.filmorate.exceptions;

public class FilmNotDetectedException extends RuntimeException{
    public FilmNotDetectedException(String message) {
        super(message);
    }
}
