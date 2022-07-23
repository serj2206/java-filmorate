package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@Repository
public class MpaDaoImpl implements MpaDao {
    public final JdbcTemplate jdbcTemplate;

    public MpaDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //Добавить рейтинг
    @Override
    public Integer add(Mpa mpa) {

        return null;
    }

    //Предоставить список рейтингов возрастных ограничений
    @Override
    public Collection<Mpa> getList() {
        log.info("  MpaDaoImpl.getList()");
        String sqlQuery = "select * from MPA";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeMpa(rs));
    }

    private Mpa makeMpa(ResultSet rs) throws SQLException {
        Mpa mpa = new Mpa();
        mpa.setId(rs.getInt("MPA_ID"));
        mpa.setName(rs.getString("MPA_NAME"));
        return mpa;
    }

    //Предоставить данные Mpa по ID
    @Override
    public Optional<Mpa> findMpaById(Integer id) {
        log.info("  MpaDaoImpl.findMpaById(id = {})", id);
        String sqlQuery = "select * from MPA where MPA_ID = ?";
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (mpaRows.next()) {
            Mpa mpa = new Mpa();
            mpa.setId(mpaRows.getInt("MPA_ID"));
            mpa.setName(mpaRows.getString("MPA_NAME"));
            return Optional.of(mpa);
        }
        return Optional.empty();
    }


}
