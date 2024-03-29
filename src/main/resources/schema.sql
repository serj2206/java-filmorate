create table GENRES
(
    GENRE_ID   INTEGER primary key AUTO_INCREMENT,
    GENRE_NAME CHARACTER VARYING not null
        constraint GENRE_UNIQUE unique
);

create table MPA
(
    MPA_ID INTEGER PRIMARY KEY AUTO_INCREMENT,
    MPA_NAME CHARACTER VARYING not null,
    MPA_DESCRIPTION CHARACTER VARYING not null
);

create table FILMS
(
    FILM_ID          INTEGER auto_increment
        primary key,
    FILM_NAME        CHARACTER VARYING(200) not null,
    FILM_DESCRIPTION CHARACTER VARYING(200) not null,
    RELEASE_DATA     DATE not null,
    DURATION         INTEGER,
    MPA_ID      CHARACTER VARYING
        references MPA (MPA_ID) ON DELETE CASCADE,
    check ("DURATION" > 0),
    constraint film_unique unique (FILM_NAME,  RELEASE_DATA)

);

create table FILM_GENRES
(
    FILM_ID  INTEGER not null
        references FILMS(FILM_ID) ON DELETE CASCADE,
    GENRE_ID INTEGER not null
        references GENRES(GENRE_ID) ON DELETE CASCADE,
    constraint FILM_GENRE_PK
        primary key (FILM_ID, GENRE_ID)
);

create table STATUS_FRIENDSHIPS
(
    STATUS_ID INTEGER not null primary key,
    STATUS_DESCRIPTION CHARACTER VARYING(20) not null
);

create table USERS
(
    USER_ID   INTEGER auto_increment
        primary key,
    USER_NAME CHARACTER VARYING(20) not null,
    LOGIN     CHARACTER VARYING(20) not null
        constraint LOGIN_UNIQUE
            unique,
    EMAIL     CHARACTER VARYING(20) not null
        constraint EMAIL_UNIQUE
            unique,
    BIRTHDAY  DATE                  not null
);

create table FRIENDSHIPS
(
    USER_ID INTEGER not null
        references USERS (USER_ID) ON DELETE CASCADE,
    FRIEND_ID INTEGER not null
        references USERS (USER_ID) ON DELETE CASCADE,
    STATUS_ID INTEGER
        references STATUS_FRIENDSHIPS,
    constraint USER_GIVE_TAKE_PK
        primary key (USER_ID, FRIEND_ID),
        check (USER_ID != FRIEND_ID)
);

create table LIKES
(
    FILM_ID INTEGER not null
        references FILMS ON DELETE CASCADE,
    USER_ID INTEGER not null
        references USERS ON DELETE CASCADE,
    constraint FILM_USER_PK
        primary key (FILM_ID, USER_ID)
);


