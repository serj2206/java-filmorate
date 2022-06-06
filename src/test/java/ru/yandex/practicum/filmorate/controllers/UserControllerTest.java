package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private final static Logger log = LoggerFactory.getLogger(UserControllerTest.class);

    private User user1;
    private User user2;
    private User user3;
    private User userReceived;

    private UserController userController;

    private ValidationException captureExceptionPOST(User user){
        final ValidationException exception = assertThrows (
                ValidationException.class, new Executable() {
                    @Override
                    public void execute() {
                        userController.create(user);
                    }
                }
        );
        return  exception;
    }

    private ValidationException captureExceptionPUT(User user){
        final ValidationException exception = assertThrows (
                ValidationException.class, new Executable() {
                    @Override
                    public void execute() {
                        userController.update(user);
                    }
                }
        );
        return  exception;
    }

    @BeforeEach
    public void begin() {
        userController = new UserController();
        user1 = User.builder().build();
        user2 = User.builder()
                .id(10)
                .email("test@yandex.ru")
                .login("Kinoman")
                .birthday(LocalDate.of(2000,1,1))
                .name("Тарантино").build();

        user3 = User.builder()
                .email("test2@yandex.ru")
                .login("Гений")
                .birthday(LocalDate.of(2000,2,2))
                .name("Фёдор")
                .build();
    }

    //***POST***
    //1 testPOST - запрос с null объектом
    @Test
    public void createUserNullTest(){
        log.debug("Запущен тест {}", "createUserNullTest");

        ValidationException exception1 = captureExceptionPOST(null);
        assertEquals("Тело запроса отсутствует.", exception1.getMessage());

        log.debug("Тест {} завершен \n", "createUserNullTest");
    }

    //2 testPOST - запрос с незаполненным user-обектом
    @Test
    public void createUserAllFieldsNullTest(){
        log.debug("Запущен тест {}", "createUserAllFieldsNullTest");

        ValidationException exception1 = captureExceptionPOST(user1);
        assertEquals("email отсутствует.", exception1.getMessage());

        log.debug("Тест {} завершен \n", "createUserAllFieldsNullTest");
    }

    //3 testPOST - запрос с заполненными полями
    @Test
    public void createUserAllFieldsTest() {
        log.debug("Запущен тест {}", "createUserAllFieldsTest()");
        userReceived = userController.create(user2);
        assertEquals(user2, userReceived, "Фактический результат НЕ равен ожидаемому : user2");
        assertTrue(user2.getId() != 10, "ID не присвоен");
        log.debug("Тест {} завершен \n", "createUserAllFieldsTest()");
    }

    //4 testPost - запрос полем email = null
    @Test
    public void createUserEmailNullTest() {
        log.debug("Запущен тест {}", "createUserEmailNull()");

        user2.setEmail(null);
        ValidationException exception1 = captureExceptionPOST(user2);
        assertEquals("email отсутствует.", exception1.getMessage());

        log.debug("Тест {} завершен \n", "createUserEmailNull()");
    }

    //5 testPost - запрос полем email =""
    @Test
    public void createUserEmailIsBlancTest() {
        log.debug("Запущен тест {}", "createUserEmailIsBlancTest()");
        user2.setEmail("");

        ValidationException exception1 = captureExceptionPOST(user2);
        assertEquals("email отсутствует.", exception1.getMessage());

        log.debug("Тест {} завершен \n", "createUserEmailIsBlancTest()");
    }

    //6 testPost - запрос с полем name = null
    @Test
    public void createUserNameNullTest() {
        log.debug("Запущен тест {}", "createUserNameNullTest()");
        user2.setName(null);
        userReceived = userController.create(user2);
        assertEquals(user2, userReceived, "Фактический результат НЕ равен ожидаемому : user2");
        log.debug("Тест {} завершен \n", "createUserEmailNull()");
    }

    //7 testPost - запрос c полем name =""
    @Test
    public void createUserNameIsBlancTest() {
        log.debug("Запущен тест {}", "createUserNameIsBlancTest()");
        user2.setName("");
        userReceived = userController.create(user2);
        assertEquals(user2, userReceived, "Фактический результат НЕ равен ожидаемому : user2");
        assertEquals(user2.getLogin(), user2.getName());

        log.debug("Тест {} завершен \n", "createUserNameIsBlancTest()");
    }

    //8 testPost - запрос с полем login = null
    @Test
    public void createUserNameNullLoginNullTest() {
        log.debug("Запущен тест {}", "createUserNameNullLoginNullTest()");

        user2.setLogin(null);

        ValidationException exception1 = captureExceptionPOST(user2);
        assertEquals("login отсутствует.", exception1.getMessage());

        log.debug("Тест {} завершен \n", "createUserNameNullLoginNullTest()");
    }

    //9 testPost - запрос c полем login = ""
    @Test
    public void createUserNameIsBlancLoginIsBlancTest() {
        log.debug("Запущен тест {}", "createUserNameIsBlancLoginIsBlancTest()");

        user2.setLogin("");
        ValidationException exception1 = captureExceptionPOST(user2);
        assertEquals("login отсутствует.", exception1.getMessage());

        log.debug("Тест {} завершен \n", "createUserNameIsBlancLoginIsBlancTest()");
    }

    //10 testPost - запрос с полем  birthday = null
    @Test
    public void createUserBirthdayNullTest() {
        log.debug("Запущен тест {}", "createUserBirthdayNullTest()");
        user2.setBirthday(null);

        ValidationException exception1 = captureExceptionPOST(user2);
        assertEquals("Дата Дня Рождения указана не верно!", exception1.getMessage());

        log.debug("Тест {} завершен \n", "createUserBirthdayNullTest()");
    }

    //11 testPost - с полем birthday, в котором указана сегодняшнего дня
    @Test
    public void createUserBirthdayNowTest() {
        log.debug("Запущен тест {}", "createUserBirthdayIsAfterNowTest()");

        user2.setBirthday(LocalDate.now());
        User userTest = userController.create(user2);
        assertEquals(user2, userTest);

        log.debug("Тест {} завершен \n", "createUserBirthdayIsAfterNowTest()");
    }



    //12 testPost - с полем birthday, в котором указана позже сегодняшнего дня
    @Test
    public void createUserBirthdayIsAfterNowTest() {
        log.debug("Запущен тест {}", "createUserBirthdayIsAfterNowTest()");
        user2.setBirthday(LocalDate.now().plusDays(1));

        ValidationException exception1 = captureExceptionPOST(user2);
        assertEquals("Дата Дня Рождения указана не верно!", exception1.getMessage());

        log.debug("Тест {} завершен \n", "createUserBirthdayIsAfterNowTest()");
    }

    //13 testPost - на добавление нескольких пользователей
    @Test
    public void createUsersTest() {
        log.debug("Запущен тест {}", "createUsersTest()");
        userReceived = userController.create(user2);
        assertEquals(user2, userReceived, "Фактический результат НЕ равен ожидаемому : user2");
        userReceived = userController.create(user3);
        assertEquals(user3, userReceived, "Фактический результат НЕ равен ожидаемому : user3");
        assertEquals(2, user3.getId(), "Фактический результат НЕ равен ожидаемому : 2");
        log.debug("Тест {} завершен \n", "createUsersTest()");
    }

    //***PUT***
    //1 testPUT - запрос с null объектом
    @Test
    public void updateUserNullTest(){
        log.debug("Запущен PUT тест {}", "updateUserNullTest");
        userController.create(user2);

        ValidationException exception1 = captureExceptionPUT(null);
        assertEquals("Тело запроса отсутствует!", exception1.getMessage());

        log.debug("Тест {} завершен \n", "updateUserNullTest");
    }

    //2 testPUT - запрос с незаполненным user-обектом
    @Test
    public void updateUserAllFieldsNullTest(){
        log.debug("Запущен PUT тест {}", "updateUserAllFieldsNullTest");

        ValidationException exception1 = captureExceptionPUT(user1);
        assertEquals("ID пользователя не найдено!", exception1.getMessage());

        log.debug("Тест {} завершен \n", "updateUserAllFieldsNullTest");
    }


    //3 testPUT - запрос с заполненными полями
    @Test
    public void updateUserAllFieldsTest() {
        log.debug("Запущен тест {}", "updateUserAllFieldsTest()");
        userController.create(user2);
        user2.setName("Zed");
        userReceived = userController.update(user2);
        assertEquals(user2, userReceived, "Фактический результат НЕ равен ожидаемому : user2");
        assertTrue(user2.getName()== "Zed", "Имя не соответствует ожидаемому");
        log.debug("Тест {} завершен \n", "updateUserAllFieldsTest()");
    }

    //4 testPUT - запрос полем email = null
    @Test
    public void updateUserEmailNullTest() {
        log.debug("Запущен тест {}", "updateUserEmailNullTest()");
        userController.create(user2);
        user2.setEmail(null);

        ValidationException exception1 = captureExceptionPUT(user2);
        assertEquals("email отсутствует!", exception1.getMessage());

        log.debug("Тест {} завершен \n", "updateUserEmailNullTest()");
    }

    //5 testPUT - запрос полем email =""
    @Test
    public void updateUserEmailIsBlancTest() {
        log.debug("Запущен тест {}", "updateUserEmailIsBlancTest()");
        userController.create(user2);

        user2.setEmail("");
        ValidationException exception1 = captureExceptionPUT(user2);

        log.debug("Тест {} завершен \n", "updateUserEmailIsBlancTest()");
    }

    //6 testPUT - запрос с полем name = null
    @Test
    public void updateUserNameNullTest() {
        log.debug("Запущен тест {}", "updateUserNameNullTest()");
        userController.create(user2);
        user2.setName(null);
        userReceived = userController.update(user2);
        assertEquals(user2, userReceived, "Фактический результат НЕ равен ожидаемому : user2");
        assertEquals(user2.getLogin(), user2.getName());
        log.debug("Тест {} завершен \n", "updateUserNameNullTest()");
    }
    //7 testPUT - запрос c полем name =""
    @Test
    public void updateUserNameIsBlancTest() {
        log.debug("Запущен тест {}", "updateUserNameIsBlancTest()");
        userController.create(user2);
        user2.setName("");
        userReceived = userController.update(user2);
        assertEquals(user2, userReceived, "Фактический результат НЕ равен ожидаемому : user2");
        assertEquals(user2.getLogin(), user2.getName());
        log.debug("Тест {} завершен \n", "updateUserNameIsBlancTest()");
    }



    //8 testPUT - запрос c полем login = ""
    @Test
    public void updateUserLoginIsBlancTest() {
        log.debug("Запущен тест {}", "updateUserLoginIsBlancTest()");
        userController.create(user2);

        user2.setLogin("");

        ValidationException exception1 = captureExceptionPUT(user2);
        assertEquals("login отсутствует.", exception1.getMessage());

        log.debug("Тест {} завершен \n", "updateUserLoginIsBlancTest()");
    }

    //9 testPUT - запрос c полем login = ""
    @Test
    public void updateUserLoginNullTest() {
        log.debug("Запущен тест {}", "updateUserLoginNullTest()");
        userController.create(user2);

        user2.setLogin(null);

        ValidationException exception1 = captureExceptionPUT(user2);
        assertEquals("login отсутствует.", exception1.getMessage());

        log.debug("Тест {} завершен \n", "updateUserLoginNullTest()");
    }

    //10testPUT - запрос с полем  birthday сег.день
    @Test
    public void updateUserBirthdayNowTest() {
        log.debug("Запущен тест {}", "updateUserBirthdayNowTest()");
        userController.create(user2);

        user2.setBirthday(LocalDate.now());

        User userTest = userController.update(user2);
        assertEquals(user2, userTest);

        log.debug("Тест {} завершен \n", "updateUserBirthdayNowTest()");
    }


    //11 testPUT - запрос с полем  birthday = null
    @Test
    public void updateUserBirthdayNullTest() {
        log.debug("Запущен тест {}", "updateUserBirthdayNullTest()");
        userController.create(user2);

        user2.setBirthday(null);

        ValidationException exception1 = captureExceptionPUT(user2);
        assertEquals("Дата Дня Рождения указана не верно!", exception1.getMessage());

        log.debug("Тест {} завершен \n", "updateUserBirthdayNullTest()");
    }

    //11 testPUT - с полем birthday, в котором дата указана позже сегодняшнего дня
    @Test
    public void updateUserBirthdayIsAfterNowTest() {
        log.debug("Запущен тест {}", "createUserBirthdayIsAfterNowTest()");
        userController.create(user2);
        user2.setBirthday(LocalDate.now().plusDays(1));

        ValidationException exception1 = captureExceptionPUT(user2);
        assertEquals("Дата Дня Рождения указана не верно!", exception1.getMessage());

        log.debug("Тест {} завершен \n", "createUserBirthdayIsAfterNowTest()");
    }

    //12 testPUT - не уазан ID
    @Test
    public void updateUserIdNotTest() {
        log.debug("Запущен тест {}", "updateUserIdNotTest()");
        userController.create(user2);

        user2.setId(0);

        ValidationException exception1 = captureExceptionPUT(user2);
        assertEquals("ID пользователя не найдено!", exception1.getMessage());

        log.debug("Тест {} завершен \n", "updateUserIdNotTest()");
    }

    //13 testPUT - уазан ID фальшивый
    @Test
    public void updateUserIdFalseTest() {
        log.debug("Запущен тест {}", "updateUserIdFalseTest()");
        userController.create(user2);
        user2.setId(10);

        ValidationException exception1 = captureExceptionPUT(user2);
        assertEquals("ID пользователя не найдено!", exception1.getMessage());

        log.debug("Тест {} завершен \n", "updateUserIdFalseTest()");
    }


    //***GET - test***

    //16 testGET - запрос на список пользователей
    @Test
    public void getListUsersTest() {
        log.debug("Запущен тест {}", "getListUsersTest()");
        userController.create(user2);
        userController.create(user3);
        ArrayList<User> listUserTest = new ArrayList<>();
        listUserTest.add(user2);
        listUserTest.add(user3);
        assertTrue(listUserTest.containsAll(userController.getList()));
        log.debug("Тест {} завершен \n", "getListUsersTest()");
    }
}