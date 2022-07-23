package ru.yandex.practicum.filmorate.storage.friendship;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Slf4j
@Component
public class FriendShipDaoImpl implements FriendShipDao {
    JdbcTemplate jdbcTemplate;

    public FriendShipDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //Найти друзей
    @Override
    public boolean findFriend(Long userId, Long friendId) {
        String sqlQuery = "select USER_ID, FRIEND_ID from FRIENDSHIPS where USER_ID = ? and FRIEND_ID = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlQuery, userId, friendId);
        if (sqlRowSet.next()) {
            log.info("Связка пользователь c id = {} и друг c id = {} найдены", sqlRowSet.getLong("USER_ID"),
                    sqlRowSet.getLong("FRIEND_ID"));
            return true;
        }
        return false;
    }

    //Добавить друга
    @Override
    public boolean add(Long userId, Long friendId) {
        String sqlQuery = "insert into FRIENDSHIPS (USER_ID, FRIEND_ID, STATUS_ID) " +
                "VALUES (?, ?, ?)";
        if (jdbcTemplate.update(sqlQuery, userId, friendId, 1) > 0) return true;
        else return false;
    }

    //Список друзей
    @Override
    public Collection<Long> getList(Long userId) {
        String sqlQuery = "select FRIEND_ID from FRIENDSHIPS where USER_ID = ? order by FRIEND_ID ";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeLong(rs), userId);

    }

    //Список общих друзей
    @Override
    public Collection<Long> getCommonFriends(Long id, Long otherId) {
        String sqlQuery = "select DISTINCT FRIEND_ID from FRIENDSHIPS where USER_ID = ? OR USER_ID = ? GROUP BY FRIEND_ID " +
                "HAVING FRIEND_ID != ? AND FRIEND_ID != ?";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeLong(rs), id, otherId, id, otherId);
    }

    private Long makeLong(ResultSet rs) throws SQLException {
        return rs.getLong("FRIEND_ID");
    }

    //Удалить друга
    @Override
    public boolean delete(Long id, Long friendId){
        String sqlQuery = "delete from FRIENDSHIPS where USER_ID =? AND FRIEND_ID = ?";
        return jdbcTemplate.update(sqlQuery, id, friendId) > 0;
    }
}
