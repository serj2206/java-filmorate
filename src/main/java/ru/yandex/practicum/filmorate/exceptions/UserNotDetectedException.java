package ru.yandex.practicum.filmorate.exceptions;

public class UserNotDetectedException extends RuntimeException{
    public UserNotDetectedException(String message) {
        super(message);
    }
}
