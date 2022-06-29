package ru.yandex.practicum.filmorate.exceptions;

public class UserNotUpdatedException extends RuntimeException{
    public UserNotUpdatedException(String message) {
        super(message);
    }
}
