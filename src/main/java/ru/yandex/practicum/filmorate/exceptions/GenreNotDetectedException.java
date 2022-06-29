package ru.yandex.practicum.filmorate.exceptions;

public class GenreNotDetectedException extends RuntimeException {
    public GenreNotDetectedException(String message) {
        super(message);
    }
}
