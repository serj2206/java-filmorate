package ru.yandex.practicum.filmorate.exceptions;

public class UserNotDeleteException extends RuntimeException{
    public UserNotDeleteException(String message) {
        super(message);
    }
}
