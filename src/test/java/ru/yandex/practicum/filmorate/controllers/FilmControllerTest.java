package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.filmorate.exceptions.FilmNotDetectedException;
import ru.yandex.practicum.filmorate.exceptions.UserNotDetectedException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    private final static Logger log = LoggerFactory.getLogger(FilmControllerTest.class);
    private Film film1;
    private Film film2;
    private Film film3;
    private FilmController filmController;
    private UserController userController;
    private User user2;
    private User user3;
    private User user4;

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

    private FilmNotDetectedException captureFilmNotDetectedExceptionPUT(Film film){
        final FilmNotDetectedException exception = assertThrows (
                FilmNotDetectedException.class, new Executable() {
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
        ValidationControl validationControl = new ValidationControl();
        FilmStorage inMemoryFilmStorage = new InMemoryFilmStorage(validationControl);
        UserStorage inMemoryUserStorage = new InMemoryUserStorage(validationControl);
        FilmService filmService = new FilmService(inMemoryFilmStorage,inMemoryUserStorage);
        filmController = new FilmController(inMemoryFilmStorage, filmService);
        UserService userService = new UserService(inMemoryUserStorage);
        userController = new UserController(inMemoryUserStorage, userService);

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

        user2 = User.builder()
                .id(10)
                .email("test@yandex.ru")
                .login("Kinoman")
                .birthday(LocalDate.of(2000,1,1))
                .name("Тарантино").build();

        user3 = User.builder()
                .email("test2@yandex.ru")
                .login("Гений")
                .birthday(LocalDate.of(2000,3,3))
                .name("Фёдор")
                .build();

        user4 = User.builder()
                .email("test3@yandex.ru")
                .login("Усатый")
                .birthday(LocalDate.of(2000,4,4))
                .name("Коля")
                .build();

    }

    //***POST - тесты***

    //1 - POST test: добавление фильма
    @Test
    public void createFilmTest() {
        log.info("Начало теста: createFilmTest");
        Film filmReceived1 = film1.toBuilder().build();
        Film filmReceived2 = film2.toBuilder().build();

        film1 = filmController.create(film1);
        film2 = filmController.create(film2);

        filmReceived1.setId(film1.getId());
        filmReceived2.setId(film2.getId());

        assertEquals(filmReceived1, film1);
        assertEquals(filmReceived2, film2);
        log.info("Конец теста: createFilmTest\n");
    }

    //2 - POST test: добавление пустого фильма
    @Test
    public void createFilmFieldsNullTest() {
        log.info("Начало теста: createFilmFieldsNullTest");
        ValidationException exception;
        exception= captureExceptionPOST(film3);

        assertEquals("Отсутствует название фильма!", exception.getMessage());
        log.info("Конец теста: createFilmFieldsNullTest\n");
    }

    //3 - POST test: отправка null обекта
    @Test
    public void createFilmNullTest() {
        log.info("Начало теста: createFilmNullTest");
        ValidationException exception;
        exception= captureExceptionPOST(null);
        assertEquals("Тело запроса отсутствует!", exception.getMessage());
        log.info("Конец теста: createFilmNullTest\n");
    }

    //4 - POST test: добавление фильма без названия
    @Test
    public void createFilmNameNullTest() {
        log.info("Начало теста: createFilmNameNullTest");
        film1.setName(null);
        ValidationException exception;
        exception= captureExceptionPOST(film1);

        assertEquals("Отсутствует название фильма!", exception.getMessage());

        film2.setName("");
        exception= captureExceptionPOST(film2);
        assertEquals("Отсутствует название фильма!", exception.getMessage());

        log.info("Конец теста: createFilmNameNullTest\n");
    }

    //5 - POST test: добавление фильма, где описание равно 200 символам
    @Test
    public void createFilmDescriptionMaxTest() {
        log.info("Начало теста: createFilmDescriptionMaxTest");
        String maxString = "012345679012345679012345679012345679012345679012345679012345679012345679012345679012345679" +
                "012345679012345679012345679012345679012345679012345679012345679012345679012345679012345679012345679" +
                "01234567901";
        log.info("Длина строки: {}", maxString.length());
        film1.setDescription(maxString);

        Film filmReceived = filmController.create(film1);
        assertEquals(filmReceived, film1);
        log.info("Конец теста: createFilmDescriptionMaxTest\n");
    }

    //5 - POST test: добавление фильма, где описание превышает 200 символов
    @Test
    public void createFilmDescriptionUpMaxTest() {
        log.info("Начало теста: createFilmDescriptionUpMaxTest");
        String maxString = "012345679012345679012345679012345679012345679012345679012345679012345679012345679012345679" +
                "012345679012345679012345679012345679012345679012345679012345679012345679012345679012345679012345679" +
                "012345679012";
        log.info("Длина строки: {}", maxString.length());
        film1.setDescription(maxString);

        ValidationException exception;
        exception= captureExceptionPOST(film1);

        assertEquals("Количество символов в описании превышает 200 символов!", exception.getMessage());
        log.info("Конец теста: createFilmDescriptionUpMaxTest\n");
    }

    //6 - POST test: добавление фильма где продолжительность равна или меньше нуля
    @Test
    public void createFilmDurationTest() {
        log.info("Начало теста: createFilmDurationTest");

        film1.setDuration(0);
        ValidationException exception1;
        exception1 = captureExceptionPOST(film1);

        assertEquals("Продолжительность фильма задана отрицательным числом или равна нулю!", exception1.getMessage());

        film1.setDuration(-1);
        ValidationException exception2;
        exception2 = captureExceptionPOST(film1);

        assertEquals("Продолжительность фильма задана отрицательным числом или равна нулю!", exception2.getMessage());
        log.info("Конец теста: createFilmDurationTest\n");
    }

    //7 - POST test: добавление фильма где дата релиза стоит раньше  28 декабря 1895года
    @Test
    public void createFilmLocalDateTest() {
        log.info("Начало теста: createFilmDurationTest");

        film1.setReleaseDate(LocalDate.of(1895, 12, 28).minusDays(1));

        ValidationException exception1;
        exception1 = captureExceptionPOST(film1);

        assertEquals("Дата представления фильма стоит до 28 декабря 1895года!", exception1.getMessage());
        log.info("Конец теста: createFilmDurationTest\n");
    }

    //***PUT - тесты***

    //1 - PUT test: Обновление фильма
    @Test
    public void updateFilmTest() {
        log.info("Начало теста: updateFilmTest");
        filmController.create(film1);
        Film filmUpdate = film1.toBuilder().description("Хороший фильм").build();
        System.out.println(filmUpdate.getName() + " id=" + filmUpdate.getId());

        Film filmReceived = filmController.update(filmUpdate);
        assertEquals(filmReceived, filmUpdate);
        assertNotEquals(filmReceived, film1);
        log.info("Конец теста: updateFilmTest\n");
    }

    //2 - PUT test: Пустой  запрос

    @Test
    public void updateFilmFieldsNullTest() {
        log.info("Начало теста: updateFilmFieldsNullTest");
        filmController.create(film1);

        ValidationException exception1;
        exception1 = captureExceptionPUT(film3);

        assertEquals("Отсутствует название фильма!", exception1.getMessage());

        log.info("Конец теста: updateFilmFieldsNullTest\n");
    }

    //3 - PUT test: отправка null обекта
    @Test
    public void updateFilmNullTest() {
        log.info("Начало теста: updateFilmNullTest");
        filmController.create(film1);

        ValidationException exception1;
        exception1 = captureExceptionPUT(null);

        assertEquals("Тело запроса отсутствует!", exception1.getMessage());

        log.info("Конец теста: updateFilmNullTest\n");
    }

    //4 - PUT test: обновление фильма с неправельным id
    @Test
    public void updateFilmIdFalseTest() {
        log.info("Начало теста: updateFilmIdFalseTest");
        filmController.create(film1);
        Film updateFilm = film1.toBuilder().id(10).build();

        FilmNotDetectedException exception1;
        exception1 = captureFilmNotDetectedExceptionPUT(updateFilm);

        assertEquals("ID фильма не найдено!", exception1.getMessage());

        log.info("Конец теста: updateFilmIdFalseTest\n");
    }

    //5 - PUT test: обновление фильма с неправильным названием
    @Test
    public void updateFilmNameFalseTest() {
        log.info("Начало теста: updateFilmNameFalseTest");
        filmController.create(film1);

        Film updateFilm = film1.toBuilder().name(null).build();
        ValidationException exception1;
        exception1 = captureExceptionPUT(updateFilm);
        assertEquals("Отсутствует название фильма!", exception1.getMessage());

        updateFilm = film1.toBuilder().name(" ").build();
        ValidationException exception2;
        exception2 = captureExceptionPUT(updateFilm);
        assertEquals("Отсутствует название фильма!", exception2.getMessage());


        log.info("Конец теста: updateFilmNameFalseTest\n");
    }

    //6 - PUT test: обновление фильма, где описание равно 200 символам
    @Test
    public void updateFilmDescriptionMaxTest() {
        log.info("Начало теста: updateFilmDescriptionMaxTest");
        filmController.create(film1);
        String maxString = "012345679012345679012345679012345679012345679012345679012345679012345679012345679012345679" +
                "012345679012345679012345679012345679012345679012345679012345679012345679012345679012345679012345679" +
                "01234567901";
        log.info("Длина строки: {}", maxString.length());
        Film updateFilm = film1.toBuilder().description(maxString).build();
        Film filmReceived = filmController.update(updateFilm);
        assertEquals(filmReceived, updateFilm);
        log.info("Конец теста: updateFilmDescriptionMaxTest\n");
    }

    //7 - PUT test: обновление фильма, где описание превышает 200 символов
    @Test
    public void updateFilmDescriptionUpMaxTest() {
        log.info("Начало теста: updateFilmDescriptionMaxTest");
        filmController.create(film1);
        String maxString = "012345679012345679012345679012345679012345679012345679012345679012345679012345679012345679" +
                "012345679012345679012345679012345679012345679012345679012345679012345679012345679012345679012345679" +
                "012345679010";
        log.info("Длина строки: {}", maxString.length());
        Film updateFilm = film1.toBuilder().description(maxString).build();

        ValidationException exception1;
        exception1 = captureExceptionPUT(updateFilm);
        assertEquals("Количество символов в описании превышает 200 символов!", exception1.getMessage());

        log.info("Конец теста: updateFilmDescriptionMaxTest\n");
    }

    //8 - PUT test: обновление фильма, где продолжительность задана меньше или равная нулю
    @Test
    public void updateFilmDurationTest() {
        log.info("Начало теста: updateFilmDurationTest");
        filmController.create(film1);

        Film updateFilm = film1.toBuilder().duration(0).build();

        ValidationException exception1;
        exception1 = captureExceptionPUT(updateFilm);
        assertEquals("Продолжительность фильма задана отрицательным числом или равна нулю!", exception1.getMessage());

        updateFilm = film1.toBuilder().duration(-1).build();

        ValidationException exception2;
        exception2 = captureExceptionPUT(updateFilm);
        assertEquals("Продолжительность фильма задана отрицательным числом или равна нулю!", exception2.getMessage());

        log.info("Конец теста: updateFilmDurationTest\n");
    }

    //9 - PUT test: обновление фильма, дата представления обозначена раньше 28 декабря 1895года
    @Test
    public void updateFilmLocalDateTest() {
        log.info("Начало теста: updateFilmLocalDateTest");

        filmController.create(film1);

        Film updateFilm = film1.toBuilder().releaseDate(LocalDate.of(1895, 12, 28).minusDays(1)).build();

        ValidationException exception1;
        exception1 = captureExceptionPUT(updateFilm);
        assertEquals("Дата представления фильма стоит до 28 декабря 1895года!", exception1.getMessage());

        log.info("Конец теста: updateFilmLocalDateTest\n");
    }

    //***GET - тесты***
    //10 - GET test: запрос списка фильмов
    @Test
    public void getFilmsTest() {
        log.info("Начало теста: updateFilmLocalDateTest");
        filmController.create(film1);
        filmController.create(film2);

        ArrayList<Film> filmArrayList = new ArrayList<>();
        filmArrayList.add(film2);
        filmArrayList.add(film1);

        assertTrue(filmArrayList.containsAll(filmController.getList()));

        log.info("Конец теста: updateFilmLocalDateTest\n");
    }

    //------------------------------------------------------------------------------------------
    //Тесты Спринт 9

    //1 - Пользователь ставит лайк фильму - штатно
    @Test
    public void addLikeTest() {
        log.info("Начало теста: addLikeTest()");
        filmController.create(film1);
        userController.create(user2);
        filmController.addLike(film1.getId(),user2.getId());
        assertTrue(film1.getLikes().contains(user2.getId()));
        log.info("Конец теста: addLikeTest()\n");
    }

    //2 - Пользователь ставит лайк фильму - id == null
    @Test
    public void addLikeIdNullTest() {
        log.info("Начало теста: addLikeIdNullTest()");
        filmController.create(film1);
        userController.create(user2);
        Integer id = null;

        final NullPointerException exception = assertThrows (
                NullPointerException.class, new Executable() {
                    @Override
                    public void execute() {
                        filmController.addLike(id,user2.getId());
                    }
                }
        );
        assertEquals(exception.getMessage(), "Id = null");

        final NullPointerException exception1 = assertThrows (
                NullPointerException.class, new Executable() {
                    @Override
                    public void execute() {
                        filmController.addLike(film1.getId(),id);
                    }
                }
        );
        assertEquals(exception1.getMessage(), "userId = null");
        log.info("Конец теста: addLikeIdNullTest()\n");
    }

    //3 - Пользователь ставит лайк фильму - id неверен
    @Test
    public void addLikeIdFalseTest() {
        log.info("Начало теста: addLikeIdFalseTest()");
        filmController.create(film1);
        userController.create(user2);
        Integer id = 10;

        final UserNotDetectedException exception = assertThrows(
                UserNotDetectedException.class, new Executable() {
                    @Override
                    public void execute() {
                        filmController.addLike(film1.getId(),id);
                    }
                }
        );
        assertEquals(exception.getMessage(), String.format("Пользователь с id %s не найден", id));

        final FilmNotDetectedException exception1 = assertThrows(
                FilmNotDetectedException.class, new Executable() {
                    @Override
                    public void execute() {
                        filmController.addLike(id,user2.getId());
                    }
                }
        );
        assertEquals(exception1.getMessage(), String.format("Фильм с id %s не найден", id));
        log.info("Конец теста: addLikeIdFalseTest()\n");
    }

    //4 - Предоставление фильма по id - штатно
    @Test
    public void  getFilmTest() {
        log.info("Начало теста: getFilmTest()");
        filmController.create(film1);
        filmController.create(film2);
        assertTrue(filmController.getFilm(film1.getId()).equals(film1));
        log.info("Конец теста: getFilmTest()\n");
    }

    //5 - Предоставление фильма по id = null
    @Test
    public void  getFilmIdNullTest() {
        log.info("Начало теста: getFilmIdNullTest()");
        filmController.create(film1);
        filmController.create(film2);
        final NullPointerException exception = assertThrows (
                NullPointerException.class, new Executable() {
                    @Override
                    public void execute() {
                        filmController.getFilm(null);
                    }
                }
        );
        assertEquals(exception.getMessage(), "Id = null");
        log.info("Конец теста: getFilmIdNullTest()\n");
    }

    //6  - Предоставление фильма по id = false
    @Test
    public void  getFilmIdFalseTest() {
        log.info("Начало теста: getFilmIdFalseTest()");
        filmController.create(film1);
        filmController.create(film2);
        Integer id = 10;
        final FilmNotDetectedException exception1 = assertThrows(
                FilmNotDetectedException.class, new Executable() {
                    @Override
                    public void execute() {
                        filmController.getFilm(id);
                    }
                }
        );
        assertEquals(exception1.getMessage(), String.format("Фильм с id %s не найден", id));
        log.info("Конец теста: getFilmIdFalseTest()\n");
    }

    //7 - //Возвращает список из первых count фильмов по количеству лайков.
    //    // Если значение параметра count не задано, верните первые 10.
    @Test
    public void getTopFilmTest() {
        log.info("Начало теста: getTopFilmTest()");
        filmController.create(film1);
        filmController.create(film2);
        userController.create(user2);
        userController.create(user3);
        filmController.addLike(film1.getId(), user2.getId());
        filmController.addLike(film1.getId(), user3.getId());
        filmController.addLike(film2.getId(), user3.getId());

        List<Film> listTopFilm = filmController.getTopFilm(5);
        assertTrue(listTopFilm.size() == 2);
        assertTrue(listTopFilm.get(0) == film1);
        assertTrue(listTopFilm.get(1) == film2);
        log.info("Конец теста: getTopFilmTest()\n");
    }

    //8 - Пользователь удаляет лайк - штатно
    @Test
    public void deleteLikeTest() {
        log.info("Начало теста: deleteLikeTest()");
        filmController.create(film1);
        filmController.create(film2);
        userController.create(user2);
        userController.create(user3);
        filmController.addLike(film1.getId(), user2.getId());
        filmController.addLike(film1.getId(), user3.getId());
        filmController.addLike(film2.getId(), user3.getId());

        filmController.deleteLike(film1.getId(), user2.getId());

        assertTrue(film1.getLikes().size() == 1);
        assertTrue(film1.getLikes().contains(user3.getId()));
        log.info("Конец теста: deleteLikeTest()\n");
    }

    //8 - Пользователь удаляет лайк - id = null
    @Test
    public void deleteLikeIdNullTest() {
        log.info("Начало теста: deleteLikeIdNullTest()");
        filmController.create(film1);
        filmController.create(film2);
        userController.create(user2);
        userController.create(user3);
        filmController.addLike(film1.getId(), user2.getId());
        filmController.addLike(film1.getId(), user3.getId());
        filmController.addLike(film2.getId(), user3.getId());
        Integer id = null;

        final NullPointerException exception = assertThrows (
                NullPointerException.class, new Executable() {
                    @Override
                    public void execute() {
                        filmController.deleteLike(id, user2.getId());
                    }
                }
        );
        assertEquals(exception.getMessage(), "Id = null");

        final NullPointerException exception1 = assertThrows (
                NullPointerException.class, new Executable() {
                    @Override
                    public void execute() {
                        filmController.deleteLike(film1.getId(), id);
                    }
                }
        );
        assertEquals(exception1.getMessage(), "userId = null");

        log.info("Конец теста: deleteLikeIdNullTest()\n");
    }

    //9 - Пользователь удаляет лайк - id = false
    @Test
    public void deleteLikeIdFalseTest() {
        log.info("Начало теста: deleteLikeIdFalseTest()");
        filmController.create(film1);
        filmController.create(film2);
        userController.create(user2);
        userController.create(user3);
        filmController.addLike(film1.getId(), user2.getId());
        filmController.addLike(film1.getId(), user3.getId());
        filmController.addLike(film2.getId(), user3.getId());
        Integer id = 10;

        final UserNotDetectedException exception = assertThrows(
                UserNotDetectedException.class, new Executable() {
                    @Override
                    public void execute() {
                        filmController.deleteLike(film1.getId(), id);
                    }
                }
        );
        assertEquals(exception.getMessage(), String.format("Пользователь с id %s не найден", id));

        final FilmNotDetectedException exception1 = assertThrows(
                FilmNotDetectedException.class, new Executable() {
                    @Override
                    public void execute() {
                        filmController.deleteLike(id, user2.getId());
                    }
                }
        );
        assertEquals(exception1.getMessage(), String.format("Фильм с id %s не найден", id));
        log.info("Конец теста: deleteLikeIdFalseTest()\n");
    }

}