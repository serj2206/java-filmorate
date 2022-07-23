package ru.yandex.practicum.filmorate.storage.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@Repository
public class GenreDaoImpl implements GenreDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDaoImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    //Поиск жанра по ID
    @Override
    public Optional<Genre> findGenreById(Integer id){
        log.info("  GenreDaoImpl.findGenreById(id = {})", id);
        String sqlQuery = "select * from GENRES where GENRE_ID = ?";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if(genreRows.next()) {
            Genre genre = new Genre();
            genre.setId(id);
            genre.setName(genreRows.getString("GENRE_NAME"));
            return Optional.of(genre);
        }
        return Optional.empty();
    }

    //Список жанров
    @Override
    public Collection<Genre> getList() {
        log.info("  GenreDaoImpl.getList()");
        String sqlQuery = "select * from GENRES";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeGenre(rs));
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        Genre genre = new Genre();
        genre.setId(rs.getInt("GENRE_ID"));
        genre.setName(rs.getString("GENRE_NAME"));
        return genre;

    }

}
