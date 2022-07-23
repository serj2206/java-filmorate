package ru.yandex.practicum.filmorate.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(value = "ru.yandex.practicum.filmorate.controllers")
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException (final ValidationException e){
        return new ErrorResponse("Ошибка валидации ", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleFilmNotDetectedException(final FilmNotDetectedException e) {
        return new ErrorResponse("ID не обнаружен ", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotDetectedException(final UserNotDetectedException e) {
        return new ErrorResponse("ID не обнаружен ", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotUpdatedException(final UserNotUpdatedException e) {
        return new ErrorResponse("Ошибка обновления ", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotDeleteException(final UserNotDeleteException e) {
        return new ErrorResponse("Ошибка удаления ", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleReplayFriendShipException(final ReplayFriendShipException e) {
        return new ErrorResponse("Ошибка добавления в друзья ", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleFriendShipNotDeleteException(final FriendShipNotDeleteException e) {
        return new ErrorResponse("Ошибка удаления из друзей ", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleFilmNotUpdatedException(final FilmNotUpdatedException e) {
        return new ErrorResponse("Ошибка обновления ", e.getMessage());
    }




}
