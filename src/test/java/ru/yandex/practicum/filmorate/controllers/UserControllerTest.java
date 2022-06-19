package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.exceptions.UserNotDetectedException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private final static Logger log = LoggerFactory.getLogger(UserControllerTest.class);

    private User user1;
    private User user2;
    private User user3;
    private User user4;

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
    private UserNotDetectedException captureUserNotDetectedExceptionPUT(User user){
        final UserNotDetectedException exception = assertThrows (
                UserNotDetectedException.class, new Executable() {
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
        ValidationControl validationControl = new ValidationControl();
        InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage(validationControl);
        UserService userService = new UserService(inMemoryUserStorage);
        userController = new UserController(inMemoryUserStorage, userService);

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

    //***POST***
    //1 testPOST - запрос с null объектом
    @Test
    public void createUserNullTest(){
        log.info("Запущен тест {}", "createUserNullTest");

        ValidationException exception1 = captureExceptionPOST(null);
        assertEquals("Тело запроса отсутствует!", exception1.getMessage());

        log.info("Тест {} завершен \n", "createUserNullTest");
    }

    //2 testPOST - запрос с незаполненным user-обектом
    @Test
    public void createUserAllFieldsNullTest(){
        log.info("Запущен тест {}", "createUserAllFieldsNullTest");

        ValidationException exception1 = captureExceptionPOST(user1);
        assertEquals("email отсутствует!", exception1.getMessage());

        log.info("Тест {} завершен \n", "createUserAllFieldsNullTest");
    }

    //3 testPOST - запрос с заполненными полями
    @Test
    public void createUserAllFieldsTest() {
        log.info("Запущен тест {}", "createUserAllFieldsTest()");
        userReceived = userController.create(user2);
        assertEquals(user2, userReceived, "Фактический результат НЕ равен ожидаемому : user2");
        assertTrue(user2.getId() != 10, "ID не присвоен");
        log.info("Тест {} завершен \n", "createUserAllFieldsTest()");
    }

    //4 testPost - запрос полем email = null
    @Test
    public void createUserEmailNullTest() {
        log.info("Запущен тест {}", "createUserEmailNull()");

        user2.setEmail(null);
        ValidationException exception1 = captureExceptionPOST(user2);
        assertEquals("email отсутствует!", exception1.getMessage());

        log.info("Тест {} завершен \n", "createUserEmailNull()");
    }

    //5 testPost - запрос полем email =""
    @Test
    public void createUserEmailIsBlancTest() {
        log.info("Запущен тест {}", "createUserEmailIsBlancTest()");
        user2.setEmail("");

        ValidationException exception1 = captureExceptionPOST(user2);
        assertEquals("email отсутствует!", exception1.getMessage());

        log.info("Тест {} завершен \n", "createUserEmailIsBlancTest()");
    }

    //6 testPost - запрос с полем name = null
    @Test
    public void createUserNameNullTest() {
        log.info("Запущен тест {}", "createUserNameNullTest()");
        user2.setName(null);
        userReceived = userController.create(user2);
        assertEquals(user2, userReceived, "Фактический результат НЕ равен ожидаемому : user2");
        log.info("Тест {} завершен \n", "createUserEmailNull()");
    }

    //7 testPost - запрос c полем name =""
    @Test
    public void createUserNameIsBlancTest() {
        log.info("Запущен тест {}", "createUserNameIsBlancTest()");
        user2.setName("");
        userReceived = userController.create(user2);
        assertEquals(user2, userReceived, "Фактический результат НЕ равен ожидаемому : user2");
        assertEquals(user2.getLogin(), user2.getName());

        log.info("Тест {} завершен \n", "createUserNameIsBlancTest()");
    }

    //8 testPost - запрос с полем login = null
    @Test
    public void createUserNameNullLoginNullTest() {
        log.info("Запущен тест {}", "createUserNameNullLoginNullTest()");

        user2.setLogin(null);

        ValidationException exception1 = captureExceptionPOST(user2);
        assertEquals("login отсутствует.", exception1.getMessage());

        log.info("Тест {} завершен \n", "createUserNameNullLoginNullTest()");
    }

    //9 testPost - запрос c полем login = ""
    @Test
    public void createUserNameIsBlancLoginIsBlancTest() {
        log.info("Запущен тест {}", "createUserNameIsBlancLoginIsBlancTest()");

        user2.setLogin("");
        ValidationException exception1 = captureExceptionPOST(user2);
        assertEquals("login отсутствует.", exception1.getMessage());

        log.info("Тест {} завершен \n", "createUserNameIsBlancLoginIsBlancTest()");
    }

    //10 testPost - запрос с полем  birthday = null
    @Test
    public void createUserBirthdayNullTest() {
        log.info("Запущен тест {}", "createUserBirthdayNullTest()");
        user2.setBirthday(null);

        ValidationException exception1 = captureExceptionPOST(user2);
        assertEquals("Дата Дня Рождения указана не верно!", exception1.getMessage());

        log.info("Тест {} завершен \n", "createUserBirthdayNullTest()");
    }

    //11 testPost - с полем birthday, в котором указана сегодняшнего дня
    @Test
    public void createUserBirthdayNowTest() {
        log.info("Запущен тест {}", "createUserBirthdayIsAfterNowTest()");

        user2.setBirthday(LocalDate.now());
        User userTest = userController.create(user2);
        assertEquals(user2, userTest);

        log.info("Тест {} завершен \n", "createUserBirthdayIsAfterNowTest()");
    }



    //12 testPost - с полем birthday, в котором указана позже сегодняшнего дня
    @Test
    public void createUserBirthdayIsAfterNowTest() {
        log.info("Запущен тест {}", "createUserBirthdayIsAfterNowTest()");
        user2.setBirthday(LocalDate.now().plusDays(1));

        ValidationException exception1 = captureExceptionPOST(user2);
        assertEquals("Дата Дня Рождения указана не верно!", exception1.getMessage());

        log.info("Тест {} завершен \n", "createUserBirthdayIsAfterNowTest()");
    }

    //13 testPost - на добавление нескольких пользователей
    @Test
    public void createUsersTest() {
        log.info("Запущен тест {}", "createUsersTest()");
        userReceived = userController.create(user2);
        assertEquals(user2, userReceived, "Фактический результат НЕ равен ожидаемому : user2");
        userReceived = userController.create(user3);
        assertEquals(user3, userReceived, "Фактический результат НЕ равен ожидаемому : user3");
        assertEquals(2, user3.getId(), "Фактический результат НЕ равен ожидаемому : 2");
        log.info("Тест {} завершен \n", "createUsersTest()");
    }

    //***PUT***
    //1 testPUT - запрос с null объектом
    @Test
    public void updateUserNullTest(){
        log.info("Запущен PUT тест {}", "updateUserNullTest");
        userController.create(user2);

        ValidationException exception1 = captureExceptionPUT(null);
        assertEquals("Тело запроса отсутствует!", exception1.getMessage());

        log.info("Тест {} завершен \n", "updateUserNullTest");
    }

    //2 testPUT - запрос с незаполненным user-обектом
    @Test
    public void updateUserAllFieldsNullTest(){
        log.info("Запущен PUT тест {}", "updateUserAllFieldsNullTest");

        ValidationException exception1 = captureExceptionPUT(user1);
        assertEquals("email отсутствует!", exception1.getMessage());

        log.info("Тест {} завершен \n", "updateUserAllFieldsNullTest");
    }


    //3 testPUT - запрос с заполненными полями
    @Test
    public void updateUserAllFieldsTest() {
        log.info("Запущен тест {}", "updateUserAllFieldsTest()");
        userController.create(user2);
        user2.setName("Zed");
        userReceived = userController.update(user2);
        assertEquals(user2, userReceived, "Фактический результат НЕ равен ожидаемому : user2");
        assertTrue(user2.getName()== "Zed", "Имя не соответствует ожидаемому");
        log.info("Тест {} завершен \n", "updateUserAllFieldsTest()");
    }

    //4 testPUT - запрос полем email = null
    @Test
    public void updateUserEmailNullTest() {
        log.info("Запущен тест {}", "updateUserEmailNullTest()");
        userController.create(user2);
        user2.setEmail(null);

        ValidationException exception1 = captureExceptionPUT(user2);
        assertEquals("email отсутствует!", exception1.getMessage());

        log.info("Тест {} завершен \n", "updateUserEmailNullTest()");
    }

    //5 testPUT - запрос полем email =""
    @Test
    public void updateUserEmailIsBlancTest() {
        log.info("Запущен тест {}", "updateUserEmailIsBlancTest()");
        userController.create(user2);

        user2.setEmail("");
        ValidationException exception1 = captureExceptionPUT(user2);

        log.info("Тест {} завершен \n", "updateUserEmailIsBlancTest()");
    }

    //6 testPUT - запрос с полем name = null
    @Test
    public void updateUserNameNullTest() {
        log.info("Запущен тест {}", "updateUserNameNullTest()");
        userController.create(user2);
        user2.setName(null);
        userReceived = userController.update(user2);
        assertEquals(user2, userReceived, "Фактический результат НЕ равен ожидаемому : user2");
        assertEquals(user2.getLogin(), user2.getName());
        log.info("Тест {} завершен \n", "updateUserNameNullTest()");
    }
    //7 testPUT - запрос c полем name =""
    @Test
    public void updateUserNameIsBlancTest() {
        log.info("Запущен тест {}", "updateUserNameIsBlancTest()");
        userController.create(user2);
        user2.setName("");
        userReceived = userController.update(user2);
        assertEquals(user2, userReceived, "Фактический результат НЕ равен ожидаемому : user2");
        assertEquals(user2.getLogin(), user2.getName());
        log.info("Тест {} завершен \n", "updateUserNameIsBlancTest()");
    }



    //8 testPUT - запрос c полем login = ""
    @Test
    public void updateUserLoginIsBlancTest() {
        log.info("Запущен тест {}", "updateUserLoginIsBlancTest()");
        userController.create(user2);

        user2.setLogin("");

        ValidationException exception1 = captureExceptionPUT(user2);
        assertEquals("login отсутствует.", exception1.getMessage());

        log.info("Тест {} завершен \n", "updateUserLoginIsBlancTest()");
    }

    //9 testPUT - запрос c полем login = ""
    @Test
    public void updateUserLoginNullTest() {
        log.info("Запущен тест {}", "updateUserLoginNullTest()");
        userController.create(user2);

        user2.setLogin(null);

        ValidationException exception1 = captureExceptionPUT(user2);
        assertEquals("login отсутствует.", exception1.getMessage());

        log.info("Тест {} завершен \n", "updateUserLoginNullTest()");
    }

    //10testPUT - запрос с полем  birthday сег.день
    @Test
    public void updateUserBirthdayNowTest() {
        log.info("Запущен тест {}", "updateUserBirthdayNowTest()");
        userController.create(user2);

        user2.setBirthday(LocalDate.now());

        User userTest = userController.update(user2);
        assertEquals(user2, userTest);

        log.info("Тест {} завершен \n", "updateUserBirthdayNowTest()");
    }


    //11 testPUT - запрос с полем  birthday = null
    @Test
    public void updateUserBirthdayNullTest() {
        log.info("Запущен тест {}", "updateUserBirthdayNullTest()");
        userController.create(user2);

        user2.setBirthday(null);

        ValidationException exception1 = captureExceptionPUT(user2);
        assertEquals("Дата Дня Рождения указана не верно!", exception1.getMessage());

        log.info("Тест {} завершен \n", "updateUserBirthdayNullTest()");
    }

    //11 testPUT - с полем birthday, в котором дата указана позже сегодняшнего дня
    @Test
    public void updateUserBirthdayIsAfterNowTest() {
        log.info("Запущен тест {}", "createUserBirthdayIsAfterNowTest()");
        userController.create(user2);
        user2.setBirthday(LocalDate.now().plusDays(1));

        ValidationException exception1 = captureExceptionPUT(user2);
        assertEquals("Дата Дня Рождения указана не верно!", exception1.getMessage());

        log.info("Тест {} завершен \n", "createUserBirthdayIsAfterNowTest()");
    }

    //12 testPUT - не уазан ID
    @Test
    public void updateUserIdNotTest() {
        log.info("Запущен тест {}", "updateUserIdNotTest()");
        userController.create(user2);

        user2.setId(0);

        UserNotDetectedException exception1 = captureUserNotDetectedExceptionPUT(user2);
        assertEquals("ID пользователя не найдено!", exception1.getMessage());

        log.info("Тест {} завершен \n", "updateUserIdNotTest()");
    }

    //13 testPUT - уазан ID фальшивый
    @Test
    public void updateUserIdFalseTest() {
        log.info("Запущен тест {}", "updateUserIdFalseTest()");
        userController.create(user2);
        user2.setId(10);

        UserNotDetectedException exception1 = captureUserNotDetectedExceptionPUT(user2);
        assertEquals("ID пользователя не найдено!", exception1.getMessage());

        log.info("Тест {} завершен \n", "updateUserIdFalseTest()");
    }


    //***GET - test***

    //16 testGET - запрос на список пользователей
    @Test
    public void getListUsersTest() {
        log.info("Запущен тест {}", "getListUsersTest()");
        userController.create(user2);
        userController.create(user3);
        ArrayList<User> listUserTest = new ArrayList<>();
        listUserTest.add(user2);
        listUserTest.add(user3);
        assertTrue(listUserTest.containsAll(userController.getList()));
        log.info("Тест {} завершен \n", "getListUsersTest()");
    }

    //------------------------------------------------------------------------------------------
    //Тесты Спринт №9

    //1 - Добавление в друзья (штатная ситуация)
    @Test
    public void addFriendTest() {
        log.info("Запущен тест {}", "getFriendTest()");
        userController.create(user2);
        userController.create(user3);
        userController.addFriend(user2.getId(),user3.getId());
        assertTrue(user2.getFriends().contains(user3.getId()));
        log.info("Тест {} завершен \n", "getListUsersTest()");
    }
    //2 - Добавление в друзья - не правильный friendId друга
    @Test
    public void addFriendIdFriendFalseTest() {
        log.info("Запущен тест {}", "getFriendIdFriendFalseTest()");
        userController.create(user2);
        userController.create(user3);
        Integer friendId = 5;
        final UserNotDetectedException exception = assertThrows (
                UserNotDetectedException.class, new Executable() {
                    @Override
                    public void execute() {
                        userController.addFriend(user2.getId(),friendId);
                    }
                }
        );
        assertEquals(exception.getMessage(), String.format("Пользователь с id %s не найден", friendId));
        assertFalse(user2.getFriends().contains(5));
        log.info("Тест {} завершен \n", "getFriendIdFriendFalseTest()");
    }

    //3 - Добавление в друзья - friendId null
    @Test
    public void addFriendIdFriendNullTest() {
        log.info("Запущен тест {}", "getFriendIdFriendNullTest()");
        userController.create(user2);
        userController.create(user3);
        Integer friendId = null;
        final NullPointerException exception = assertThrows (
                NullPointerException.class, new Executable() {
                    @Override
                    public void execute() {
                        userController.addFriend(user2.getId(),friendId);
                    }
                }
        );
        assertEquals(exception.getMessage(), "friendId = null");
        assertFalse(user2.getFriends().contains(5));
        log.info("Тест {} завершен \n", "getFriendIdFriendNullTest()");
    }

    //4 - Добавления в друзья - id пользователя не верен
    @Test
    public void addFriendFalseIdFriendTest() {
        log.info("Запущен тест {}", "getFriendIdFriendFalseTest()");
        userController.create(user2);
        userController.create(user3);
        Integer id = 5;
        final UserNotDetectedException exception = assertThrows (
                UserNotDetectedException.class, new Executable() {
                    @Override
                    public void execute() {
                        userController.addFriend(id, user3.getId());
                    }
                }
        );
        assertEquals(exception.getMessage(), String.format("Пользователь с id %s не найден", id));
        assertFalse(user2.getFriends().contains(5));
        log.info("Тест {} завершен \n", "getFriendIdFriendFalseTest()");
    }

    //5 - Добавления в друзья - id пользователя null
    @Test
    public void addFriendNullIdFriendTest() {
        log.info("Запущен тест {}", "getFriendNullIdFriendTest()");
        userController.create(user2);
        userController.create(user3);
        Integer id = null;
        final NullPointerException exception = assertThrows (
                NullPointerException.class, new Executable() {
                    @Override
                    public void execute() {
                        userController.addFriend(id, user3.getId());
                    }
                }
        );
        assertEquals(exception.getMessage(), "Id = null");
        assertFalse(user2.getFriends().contains(5));
        log.info("Тест {} завершен \n", "getFriendNullIdFriendTest()");
    }

    //6 - Предоставление пользователя по id - штатная ситуация
    @Test
    public void getUserTest() {
        log.info("Запущен тест {}", "getUserTest()");
        userController.create(user2);
        userController.create(user3);
        assertTrue(userController.getUser(user2.getId()).equals(user2));
        assertTrue(userController.getUser(user3.getId()).equals(user3));
        log.info("Тест {} завершен \n", "getUserTest()");
    }

    //7 - Предоставление пользователя по id = null
    @Test
    public void getUserIdNullTest() {
        log.info("Запущен тест {}", "getUserIdNullTest()");
        userController.create(user2);
        userController.create(user3);
        Integer id = null;
        final NullPointerException exception = assertThrows (
                NullPointerException.class, new Executable() {
                    @Override
                    public void execute() {
                        userController.getUser(id);
                    }
                }
        );
        assertEquals(exception.getMessage(), "Id = null");

        log.info("Тест {} завершен \n", "getUserIdNullTest()");
    }

    //8 - Предоставление пользователя - id пользователя не верен
    @Test
    public void getUserIdFalseTest() {
        log.info("Запущен тест {}", "getUserIdFalseTest()");
        userController.create(user2);
        userController.create(user3);
        Integer id = 5;
        final UserNotDetectedException exception = assertThrows (
                UserNotDetectedException.class, new Executable() {
                    @Override
                    public void execute() {
                        userController.getUser(id);
                    }
                }
        );
        assertEquals(exception.getMessage(), String.format("Пользователь с id %s не найден", id));
        log.info("Тест {} завершен \n", "getUserIdFalseTest()");
    }

    //9 - Предоставление списка друзей пользователя - штатная ситуация
    @Test
    public void getListFriendsTest() {
        log.info("Запущен тест {}", "getListFriendsTest()");
        userController.create(user2);
        userController.create(user3);
        userController.create(user4);
        userController.addFriend(user2.getId(), user3.getId());
        userController.addFriend(user2.getId(), user4.getId());

        List<User> listFriendsUser2 = userController.getListFriends(user2.getId());
        List<User> listFriendsUser3 = userController.getListFriends(user3.getId());
        List<User> listFriendsUser4 = userController.getListFriends(user4.getId());

        assertTrue(listFriendsUser2.size() == 2);
        assertTrue(listFriendsUser3.size() == 1);
        assertTrue(listFriendsUser4.size() == 1);

        assertTrue(listFriendsUser2.contains(user3));
        assertTrue(listFriendsUser2.contains(user4));

        assertTrue(listFriendsUser3.contains(user2));
        assertTrue(listFriendsUser4.contains(user2));

        log.info("Тест {} завершен \n", "getListFriendsTest()");
    }

    //10 - Предоставление списка друзей пользователя - id = null
    @Test
    public void getListFriendsIdNullTest() {
        log.info("Запущен тест {}", "getListFriendsIdNullTest()");
        userController.create(user2);
        userController.create(user3);
        userController.create(user4);
        userController.addFriend(user2.getId(), user3.getId());
        userController.addFriend(user2.getId(), user4.getId());
        Integer id = null;
        final NullPointerException exception = assertThrows (
                NullPointerException.class, new Executable() {
                    @Override
                    public void execute() {
                        userController.getListFriends(id);
                    }
                }
        );
        assertEquals(exception.getMessage(), "Id = null");
        log.info("Тест {} завершен \n", "getListFriendsIdNullTest()");
    }

    //11 - Предоставление списка друзей пользователя - id = не верно
    @Test
    public void getListFriendsIdFalseTest() {
        log.info("Запущен тест {}", "getListFriendsIdFalseTest()");
        userController.create(user2);
        userController.create(user3);
        userController.create(user4);
        userController.addFriend(user2.getId(), user3.getId());
        userController.addFriend(user2.getId(), user4.getId());
        Integer id = 10;
        final UserNotDetectedException exception = assertThrows (
                UserNotDetectedException.class, new Executable() {
                    @Override
                    public void execute() {
                        userController.getListFriends(id);
                    }
                }
        );
        assertEquals(exception.getMessage(),String.format("Пользователь с id %s не найден", id));
        log.info("Тест {} завершен \n", "getListFriendsIdFalseTest()");
    }

    //12 - Предоставление списка друзей являющихся общими с его друзьями
    @Test
    public void getCommonFriendsTest() {
        log.info("Запущен тест {}", "getCommonFriendsTest()");
        userController.create(user2);
        userController.create(user3);
        userController.create(user4);
        userController.addFriend(user2.getId(), user3.getId());
        userController.addFriend(user2.getId(), user4.getId());
        userController.addFriend(user4.getId(),user3.getId());
        List<User> commonFriends = userController.getCommonFriends(user2.getId(),user4.getId());
        assertTrue(commonFriends.size() == 1);
        assertTrue(commonFriends.contains(user3));
        log.info("Тест {} завершен \n", "getCommonFriendsTest()");        
    }

    //13 - Предоставление списка друзей являющихся общими с его друзьями null id
    @Test
    public void getCommonFriendsIdNullTest() {
        log.info("Запущен тест {}", "getCommonFriendsIdNullTest()");
        userController.create(user2);
        userController.create(user3);
        userController.create(user4);
        userController.addFriend(user2.getId(), user3.getId());
        userController.addFriend(user2.getId(), user4.getId());
        userController.addFriend(user4.getId(),user3.getId());
        Integer id = null;
        final NullPointerException exception = assertThrows (
                NullPointerException.class, new Executable() {
                    @Override
                    public void execute() {
                        userController.getCommonFriends(null,user4.getId());
                    }
                }
        );
        assertEquals(exception.getMessage(), "Id = null");

        final NullPointerException exception1 = assertThrows (
                NullPointerException.class, new Executable() {
                    @Override
                    public void execute() {
                        userController.getCommonFriends(user4.getId(),null);
                    }
                }
        );
        assertEquals(exception1.getMessage(), "otherId = null");

        log.info("Тест {} завершен \n", "getCommonFriendsIdNullTest()");
    }

    //14 - Предоставление списка друзей являющихся общими с его друзьями  id не верный
    @Test
    public void getCommonFriendsIdFalseTest() {
        log.info("Запущен тест {}", "getCommonFriendsIdFalseTest()");
        userController.create(user2);
        userController.create(user3);
        userController.create(user4);
        userController.addFriend(user2.getId(), user3.getId());
        userController.addFriend(user2.getId(), user4.getId());
        userController.addFriend(user4.getId(),user3.getId());
        Integer id = 10;
        final UserNotDetectedException exception = assertThrows (
                UserNotDetectedException.class, new Executable() {
                    @Override
                    public void execute() {
                        userController.getCommonFriends(id,user4.getId());
                    }
                }
        );
        assertEquals(exception.getMessage(),String.format("Пользователь с id %s не найден", id));
        final UserNotDetectedException exception1 = assertThrows (
                UserNotDetectedException.class, new Executable() {
                    @Override
                    public void execute() {
                        userController.getCommonFriends(user4.getId(),id);
                    }
                }
        );
        assertEquals(exception1.getMessage(),String.format("Пользователь с id %s не найден", id));

        log.info("Тест {} завершен \n", "getCommonFriendsIdFalseTest()");
    }

    //15 - Удаление из друзей - штатно
    @Test
    public void deleteFriendTest() {
        log.info("Запущен тест {}", "deleteFriendTest()");
        userController.create(user2);
        userController.create(user3);
        userController.create(user4);
        userController.addFriend(user2.getId(), user3.getId());
        userController.addFriend(user2.getId(), user4.getId());
        userController.deleteFriend(user2.getId(), user3.getId());
        assertFalse(userController.getListFriends(user2.getId()).contains(user3));
        assertFalse(userController.getListFriends(user3.getId()).contains(user2));
        assertTrue(userController.getListFriends(user2.getId()).contains(user4));
        assertTrue(userController.getListFriends(user2.getId()).size() == 1);
        log.info("Тест {} завершен \n", "deleteFriendTest()");
    }

    //16 - удаление из друзей id = null
    @Test
    public void deleteFriendIdNullTest() {
        log.info("Запущен тест {}", "deleteFriendIdNullTest()");
        userController.create(user2);
        userController.create(user3);
        userController.create(user4);
        userController.addFriend(user2.getId(), user3.getId());
        userController.addFriend(user2.getId(), user4.getId());
        Integer id = null;
        final NullPointerException exception = assertThrows (
                NullPointerException.class, new Executable() {
                    @Override
                    public void execute() {
                        userController.deleteFriend(null, user3.getId());;
                    }
                }
        );
        assertEquals(exception.getMessage(), "Id = null");

        final NullPointerException exception1 = assertThrows (
                NullPointerException.class, new Executable() {
                    @Override
                    public void execute() {
                        userController.deleteFriend(user2.getId(), null);
                    }
                }
        );
        assertEquals(exception1.getMessage(), "otherId = null");

        log.info("Тест {} завершен \n", "deleteFriendIdNullTest()");
    }
    //17 - удаление из друзей id = неверный
    @Test
    public void deleteFriendIdFalseTest() {
        log.info("Запущен тест {}", "deleteFriendIdFalseTest()");
        userController.create(user2);
        userController.create(user3);
        userController.create(user4);
        userController.addFriend(user2.getId(), user3.getId());
        userController.addFriend(user2.getId(), user4.getId());
        Integer id = 10;
        final UserNotDetectedException exception = assertThrows(
                UserNotDetectedException.class, new Executable() {
                    @Override
                    public void execute() {
                        userController.deleteFriend(user2.getId(), id);
                    }
                }
        );
        assertEquals(exception.getMessage(), String.format("Пользователь с id %s не найден", id));
        final UserNotDetectedException exception1 = assertThrows(
                UserNotDetectedException.class, new Executable() {
                    @Override
                    public void execute() {
                        userController.deleteFriend(id, user3.getId());
                    }
                }
        );
        assertEquals(exception1.getMessage(), String.format("Пользователь с id %s не найден", id));
        log.info("Тест {} завершен \n", "deleteFriendIdFalseTest()");
    }
}