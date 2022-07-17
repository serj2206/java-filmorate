package ru.yandex.practicum.filmorate.storage.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controllers.ValidationControl;
import ru.yandex.practicum.filmorate.exceptions.UserNotDetectedException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Component("UserDbStorage")
public class UserDbStorage implements UserStorage {
    JdbcTemplate jdbcTemplate;


    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User add(User user) {
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

        return getUser(keyHolder.getKey().longValue());
    }

    @Override
    public User delete(Long id) {
        User user = getUser(id);
        String sqlQuery = "delete from USERS where USER_ID=?";
        if(jdbcTemplate.update(sqlQuery, id) > 0) return user;
        return null;
    }

    @Override
    public User update(User user) {
        String sqlQuery = "update USERS set USER_NAME=?, LOGIN=?, EMAIL=?, BIRTHDAY=? where USER_ID=?";
        jdbcTemplate.update(sqlQuery, user.getName(), user.getLogin(), user.getEmail(), user.getBirthday().toString(),
                user.getId());

        return getUser(user.getId());
    }

    @Override
    public Collection<User> getList() {
        return null;
    }


    public Optional<User> findUserByID(Long id) {
        String sql = "select * from USERS where USER_ID=?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql, id);
        if(userRows.next()) {
            User user = User.builder()
                    .id(userRows.getLong("USER_ID"))
                    .name(userRows.getString("NAME"))
                    .login(userRows.getString("LOGIN"))
                    .email(userRows.getString("EMAIL"))
                    .birthday(userRows.getDate("BIRTHDAY").toLocalDate())
                    .build();
            return Optional.of(user);
        }
        return Optional.empty();
    }

    @Override
    public User getUser(Long id) {
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
