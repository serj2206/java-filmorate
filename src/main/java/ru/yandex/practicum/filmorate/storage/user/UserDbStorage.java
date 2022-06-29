package ru.yandex.practicum.filmorate.storage.user;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@Component("UserDbStorage")
public class UserDbStorage implements UserStorage {
    JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long add(User user) {
        log.debug("  UserDbStorage.add()");

        String sqlQuery = "insert into USERS (USER_NAME, LOGIN, EMAIL, BIRTHDAY) values (?, ?, ?, ?)";
        //jdbcTemplate.update(sqlQuery, user.getName(), user.getLogin(), user.getEmail(), user.getBirthday().toString());
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
                    PreparedStatement stmt =
                            connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
                    stmt.setString(1, user.getName());
                    stmt.setString(2, user.getLogin());
                    stmt.setString(3, user.getEmail());
                    stmt.setString(4, user.getBirthday().toString());
                    return stmt;
                },
                keyHolder);

        return (keyHolder.getKey().longValue());
    }

    @Override
    public boolean delete(Long id) {
        log.debug("  UserDbStorage.delete(id = {})", id);
        String sqlQuery = "delete from USERS where USER_ID=?";
        if(jdbcTemplate.update(sqlQuery, id) > 0) return true;
        return false;
    }

    @Override
    public boolean update(User user) {
        log.debug("  UserDbStorage.update()");
        String sqlQuery = "update USERS set USER_NAME=?, LOGIN=?, EMAIL=?, BIRTHDAY=? where USER_ID=?";
        if (jdbcTemplate.update(
                sqlQuery,
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                user.getBirthday().toString(),
                user.getId()
        ) > 0) return true;
        else return false;
    }


    @Override
    public Optional<User> findUserById(Long id) {
        log.debug("  UserDbStorage.findUserByID(id = {})", id);
        String sql = "select * from USERS where USER_ID=?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql, id);
        if(userRows.next()) {
            User user = User.builder()
                    .id(userRows.getLong("USER_ID"))
                    .name(userRows.getString("USER_NAME"))
                    .login(userRows.getString("LOGIN"))
                    .email(userRows.getString("EMAIL"))
                    .birthday(userRows.getDate("BIRTHDAY").toLocalDate())
                    .build();
            return Optional.of(user);
        }
        return Optional.empty();
    }

    //Список пользователей
    @Override
    public Collection<User> getList() {
        log.debug("  UserDbStorage.getList()");
        String sqlQuery = "select * from USERS ORDER BY USER_ID";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeUser(rs));
    }
    @Override
    public User getUser(Long id) {
        log.debug("  UserDbStorage.getUser(id = {})", id);
        return null;
    }


    private User makeUser(ResultSet rs) throws SQLException {
        Long id = rs.getLong("USER_ID");
        String email = rs.getString("EMAIL");
        String name = rs.getString("USER_NAME");
        String login = rs.getString("LOGIN");
        LocalDate birthday = rs.getDate("BIRTHDAY").toLocalDate();
        return new User(id, email, login, name, birthday);
    }
}
