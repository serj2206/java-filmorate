MERGE INTO MPA (MPA_ID, MPA_NAME, MPA_DESCRIPTION)
    VALUES (1, 'G', 'Нет возрастных ограничений'),
    (2, 'PG', 'Детям рекомендуется смотреть фильм с родителями'),
    (3, 'PG-13', 'Детям до 13 лет просмотр не желателен'),
    (4, 'R', 'Лицам до 17 лет просматривать фильм можно только в присутствии взрослого'),
    (5, 'NC-17', 'Лицам до 18 лет просмотр запрещён');

MERGE INTO GENRES (GENRE_ID, GENRE_NAME)
VALUES (1, 'Комедия' ),
       (2, 'Драма'),
       (3, 'Мультфильм'),
       (4, 'Триллер'),
       (5, 'Документальный'),
       (6, 'Боевик');

MERGE INTO STATUS_FRIENDSHIPS (STATUS_ID, STATUS_DESCRIPTION) VALUES ( 1, 'FRIEND' );

--MERGE INTO USERS (USER_ID, USER_NAME, LOGIN, EMAIL, BIRTHDAY)
--VALUES (0, 'Peter', 'peter_log', 'peter@yandex.ru', '2000-10-15');





