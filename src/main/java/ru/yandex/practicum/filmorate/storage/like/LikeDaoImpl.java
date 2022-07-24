package ru.yandex.practicum.filmorate.storage.like;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
@Slf4j
@Repository
public class LikeDaoImpl implements LikeDao {
    private final JdbcTemplate jdbcTemplate;


    public LikeDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //Добавить Like
    @Override
    public boolean add(Long filmId, Long userId) {
        log.info("  LikeDaoImpl.add(filmId = {}, userId = {})", filmId, userId);
        String sqlQuery = "insert into LIKES (FILM_ID, USER_ID) values (?, ?)";
        if(jdbcTemplate.update(sqlQuery, filmId, userId) > 0) return true;
        return false;
    }

    //Удаление лайка
    @Override
    public boolean delete(Long filmId, Long userId) {
        log.info("  LikeDaoImpl.delete(filmId = {}, userId = {})", filmId, userId);
        String sqlQuery = "delete from LIKES where FILM_ID = ? AND USER_ID = ?";
        if (jdbcTemplate.update(sqlQuery, filmId, userId) == 1) return true;
        return false;
    }

    //Популярные фильмы
    @Override
    public Collection<Long> getTop(Integer count){
        log.info("  LikeDaoImpl.getTop()");
        String sqlQuery = "select DISTINCT (F.FILM_ID), count(L.USER_ID) AS count_likes from LIKES as L RIGHT JOIN FILMS as F on L.FILM_ID = F.FILM_ID " +
                "group by F.FILM_ID " +
                "ORDER BY count_likes DESC, F.FILM_ID " +
                "limit ?";
        return jdbcTemplate.query(sqlQuery, (rs, rowNUm) -> makeLike(rs), count);
    }

    private Long makeLike(ResultSet rs) throws SQLException {
        return rs.getLong("FILM_ID");
    }
}
