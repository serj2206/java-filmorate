package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    private final static Logger log = LoggerFactory.getLogger(FilmControllerTest.class);
    private Film film1;
    private Film film2;
    private Film film3;
    private FilmController filmController;

    private ValidationException captureExceptionPOST(Film film){
        final ValidationException exception = assertThrows (
                ValidationException.class, new Executable() {
                    @Override
                    public void execute() {
                        filmController.create(film);
                    }
                }
        );
        return  exception;
    }

    private ValidationException captureExceptionPUT(Film film){
        final ValidationException exception = assertThrows (
                ValidationException.class, new Executable() {
                    @Override
                    public void execute() {
                        filmController.update(film);
                    }
                }
        );
        return  exception;
    }

    @BeforeEach
    public void begin() {
        film1 = Film.builder()
                .name("Ракета")
                .description("Полеты во сне и на яву")
                .releaseDate(LocalDate.of(1980, 2, 25))
                .duration(45)
                .build();

        film2 = Film.builder()
                .name("Радуга")
                .description("Добежать до радуги и вернуться назад")
                .releaseDate(LocalDate.of(1990, 3, 30))
                .duration(100)
                .build();

        film3 = Film.builder().build();

        filmController = new FilmController();
    }

    //***POST - тесты***

    //1 - POST test: добавление фильма
    @Test
    public void createFilmTest() {
        log.debug("Начало теста: createFilmTest");
        Film filmReceived1 = film1.toBuilder().build();
        Film filmReceived2 = film2.toBuilder().build();

        film1 = filmController.create(film1);
        film2 = filmController.create(film2);

        filmReceived1.setId(film1.getId());
        filmReceived2.setId(film2.getId());

        assertEquals(filmReceived1, film1);
        assertEquals(filmReceived2, film2);
        log.debug("Конец теста: createFilmTest\n");
    }

    //2 - POST test: добавление пустого фильма
    @Test
    public void createFilmFieldsNullTest() {
        log.debug("Начало теста: createFilmFieldsNullTest");
        ValidationException exception;
        exception= captureExceptionPOST(film3);

        assertEquals("Отсутствует название фильма!", exception.getMessage());
        log.debug("Конец теста: createFilmFieldsNullTest\n");
    }

    //3 - POST test: отправка null обекта
    @Test
    public void createFilmNullTest() {
        log.debug("Начало теста: createFilmNullTest");
        ValidationException exception;
        exception= captureExceptionPOST(null);
        assertEquals("Тело запроса отсутствует!", exception.getMessage());
        log.debug("Конец теста: createFilmNullTest\n");
    }

    //4 - POST test: добавление фильма без названия
    @Test
    public void createFilmNameNullTest() {
        log.debug("Начало теста: createFilmNameNullTest");
        film1.setName(null);
        ValidationException exception;
        exception= captureExceptionPOST(film1);

        assertEquals("Отсутствует название фильма!", exception.getMessage());

        film2.setName("");
        exception= captureExceptionPOST(film2);
        assertEquals("Отсутствует название фильма!", exception.getMessage());

        log.debug("Конец теста: createFilmNameNullTest\n");
    }

    //5 - POST test: добавление фильма, где описание равно 200 символам
    @Test
    public void createFilmDescriptionMaxTest() {
        log.debug("Начало теста: createFilmDescriptionMaxTest");
        String maxString = "012345679012345679012345679012345679012345679012345679012345679012345679012345679012345679" +
                "012345679012345679012345679012345679012345679012345679012345679012345679012345679012345679012345679" +
                "01234567901";
        log.debug("Длина строки: {}", maxString.length());
        film1.setDescription(maxString);

        Film filmReceived = filmController.create(film1);
        assertEquals(filmReceived, film1);
        log.debug("Конец теста: createFilmDescriptionMaxTest\n");
    }

    //5 - POST test: добавление фильма, где описание превышает 200 символов
    @Test
    public void createFilmDescriptionUpMaxTest() {
        log.debug("Начало теста: createFilmDescriptionUpMaxTest");
        String maxString = "012345679012345679012345679012345679012345679012345679012345679012345679012345679012345679" +
                "012345679012345679012345679012345679012345679012345679012345679012345679012345679012345679012345679" +
                "012345679012";
        log.debug("Длина строки: {}", maxString.length());
        film1.setDescription(maxString);

        ValidationException exception;
        exception= captureExceptionPOST(film1);

        assertEquals("Количество символов в описании превышает 200 символов!", exception.getMessage());
        log.debug("Конец теста: createFilmDescriptionUpMaxTest\n");
    }

    //6 - POST test: добавление фильма где продолжительность равна или меньше нуля
    @Test
    public void createFilmDurationTest() {
        log.debug("Начало теста: createFilmDurationTest");

        film1.setDuration(0);
        ValidationException exception1;
        exception1 = captureExceptionPOST(film1);

        assertEquals("Продолжительность фильма задана отрицательным числом или равна нулю!", exception1.getMessage());

        film1.setDuration(-1);
        ValidationException exception2;
        exception2 = captureExceptionPOST(film1);

        assertEquals("Продолжительность фильма задана отрицательным числом или равна нулю!", exception2.getMessage());
        log.debug("Конец теста: createFilmDurationTest\n");
    }

    //7 - POST test: добавление фильма где дата релиза стоит раньше  28 декабря 1895года
    @Test
    public void createFilmLocalDateTest() {
        log.debug("Начало теста: createFilmDurationTest");

        film1.setReleaseDate(LocalDate.of(1895, 12, 28).minusDays(1));

        ValidationException exception1;
        exception1 = captureExceptionPOST(film1);

        assertEquals("Дата представления фильма стоит до 28 декабря 1895года!", exception1.getMessage());
        log.debug("Конец теста: createFilmDurationTest\n");
    }

    //***PUT - тесты***

    //1 - PUT test: Обновление фильма
    @Test
    public void updateFilmTest() {
        log.debug("Начало теста: updateFilmTest");
        filmController.create(film1);
        Film filmUpdate = film1.toBuilder().description("Хороший фильм").build();
        System.out.println(filmUpdate.getName() + " id=" + filmUpdate.getId());

        Film filmReceived = filmController.update(filmUpdate);
        assertEquals(filmReceived, filmUpdate);
        assertNotEquals(filmReceived, film1);
        log.debug("Конец теста: updateFilmTest\n");
    }

    //2 - PUT test: Пустой  запрос

    @Test
    public void updateFilmFieldsNullTest() {
        log.debug("Начало теста: updateFilmFieldsNullTest");
        filmController.create(film1);

        ValidationException exception1;
        exception1 = captureExceptionPUT(film3);

        assertEquals("Отсутствует название фильма!", exception1.getMessage());

        log.debug("Конец теста: updateFilmFieldsNullTest\n");
    }

    //3 - PUT test: отправка null обекта
    @Test
    public void updateFilmNullTest() {
        log.debug("Начало теста: updateFilmNullTest");
        filmController.create(film1);

        ValidationException exception1;
        exception1 = captureExceptionPUT(null);

        assertEquals("Тело запроса отсутствует!", exception1.getMessage());

        log.debug("Конец теста: updateFilmNullTest\n");
    }

    //4 - PUT test: обновление фильма с неправельным id
    @Test
    public void updateFilmIdFalseTest() {
        log.debug("Начало теста: updateFilmIdFalseTest");
        filmController.create(film1);
        Film updateFilm = film1.toBuilder().id(10).build();

        ValidationException exception1;
        exception1 = captureExceptionPUT(updateFilm);

        assertEquals("ID фильма не найдено!", exception1.getMessage());

        log.debug("Конец теста: updateFilmIdFalseTest\n");
    }

    //5 - PUT test: обновление фильма с неправильным названием
    @Test
    public void updateFilmNameFalseTest() {
        log.debug("Начало теста: updateFilmNameFalseTest");
        filmController.create(film1);

        Film updateFilm = film1.toBuilder().name(null).build();
        ValidationException exception1;
        exception1 = captureExceptionPUT(updateFilm);
        assertEquals("Отсутствует название фильма!", exception1.getMessage());

        updateFilm = film1.toBuilder().name(" ").build();
        ValidationException exception2;
        exception2 = captureExceptionPUT(updateFilm);
        assertEquals("Отсутствует название фильма!", exception2.getMessage());


        log.debug("Конец теста: updateFilmNameFalseTest\n");
    }

    //6 - PUT test: обновление фильма, где описание равно 200 символам
    @Test
    public void updateFilmDescriptionMaxTest() {
        log.debug("Начало теста: updateFilmDescriptionMaxTest");
        filmController.create(film1);
        String maxString = "012345679012345679012345679012345679012345679012345679012345679012345679012345679012345679" +
                "012345679012345679012345679012345679012345679012345679012345679012345679012345679012345679012345679" +
                "01234567901";
        log.debug("Длина строки: {}", maxString.length());
        Film updateFilm = film1.toBuilder().description(maxString).build();
        Film filmReceived = filmController.update(updateFilm);
        assertEquals(filmReceived, updateFilm);
        log.debug("Конец теста: updateFilmDescriptionMaxTest\n");
    }

    //7 - PUT test: обновление фильма, где описание превышает 200 символов
    @Test
    public void updateFilmDescriptionUpMaxTest() {
        log.debug("Начало теста: updateFilmDescriptionMaxTest");
        filmController.create(film1);
        String maxString = "012345679012345679012345679012345679012345679012345679012345679012345679012345679012345679" +
                "012345679012345679012345679012345679012345679012345679012345679012345679012345679012345679012345679" +
                "012345679010";
        log.debug("Длина строки: {}", maxString.length());
        Film updateFilm = film1.toBuilder().description(maxString).build();

        ValidationException exception1;
        exception1 = captureExceptionPUT(updateFilm);
        assertEquals("Количество символов в описании превышает 200 символов!", exception1.getMessage());

        log.debug("Конец теста: updateFilmDescriptionMaxTest\n");
    }

    //8 - PUT test: обновление фильма, где продолжительность задана меньше или равная нулю
    @Test
    public void updateFilmDurationTest() {
        log.debug("Начало теста: updateFilmDurationTest");
        filmController.create(film1);

        Film updateFilm = film1.toBuilder().duration(0).build();

        ValidationException exception1;
        exception1 = captureExceptionPUT(updateFilm);
        assertEquals("Продолжительность фильма задана отрицательным числом или равна нулю!", exception1.getMessage());

        updateFilm = film1.toBuilder().duration(-1).build();

        ValidationException exception2;
        exception2 = captureExceptionPUT(updateFilm);
        assertEquals("Продолжительность фильма задана отрицательным числом или равна нулю!", exception2.getMessage());

        log.debug("Конец теста: updateFilmDurationTest\n");
    }

    //9 - PUT test: обновление фильма, дата представления обозначена раньше 28 декабря 1895года
    @Test
    public void updateFilmLocalDateTest() {
        log.debug("Начало теста: updateFilmLocalDateTest");

        filmController.create(film1);

        Film updateFilm = film1.toBuilder().releaseDate(LocalDate.of(1895, 12, 28).minusDays(1)).build();

        ValidationException exception1;
        exception1 = captureExceptionPUT(updateFilm);
        assertEquals("Дата представления фильма стоит до 28 декабря 1895года!", exception1.getMessage());

        log.debug("Конец теста: updateFilmLocalDateTest\n");
    }

    //***GET - тесты***
    //10 - GET test: запрос списка фильмов
    @Test
    public void getFilmsTest() {
        log.debug("Начало теста: updateFilmLocalDateTest");
        filmController.create(film1);
        filmController.create(film2);

        ArrayList<Film> filmArrayList = new ArrayList<>();
        filmArrayList.add(film2);
        filmArrayList.add(film1);

        assertTrue(filmArrayList.containsAll(filmController.getList()));

        log.debug("Конец теста: updateFilmLocalDateTest\n");
    }
}