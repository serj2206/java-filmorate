# java-filmorate
Template repository for Filmorate project.
![Диаграмма БД](/../add-database/Diagramma.png)

  

## Описание  

### Таблицы

- **User** - содержит данные пользователя
- **Friends** - кто кого пригласил в друзья и их статус
- **Status_Friendship** - таблица возможных статусов дружбы
- **Films** - содержит данные о фильмах
- **Like** - содержит ID фильма и ID пользователя, который поставил like этому фильму
- **Genre_Film** - Жанр фильма: ID фильма и ID жанра
- **Genres** - Таблица возможных жанров
- **Age_Rating** - Таблица возможных рейтингов возрастных ограничений
- **Rating_Film** - Таблица присвоения рейтинга фильму

## Примеры запросов

### Выборка всех фильмов
__SELECT__ *  
__FROM__ *films*;

### Получение списка всех пользователей
__SELECT__ *  
__FROM__ *users*;

### Топ N наиболее популярных фильмов
__SELECT__ *f.id_films* __AS__ *id*, --ID фильма  
*f.name* __AS__ *name_film*,   --Название фильма  
__SUM__(*l.id_user*) __AS__ *count_like*   --Количество лайков фильма  
-- Объединение таблицы фильмов и таблицы лайков:  
-- В итоговую таблицу войдут только те значениея, где совпадает ID фильмов  
__FROM__ *films* __AS__ *f* __INNER JOIN__ *like* __AS__ *l* __ON__ *f.id_films = l.id_films*  
__GROUP BY__ *id* --группировка по ID фильма  
__ORDER BY__ *count_like DESC* --Сортировка по количеству лайков от большего к меньшему  
__LIMIT__ *10*; --Указываем топ N
