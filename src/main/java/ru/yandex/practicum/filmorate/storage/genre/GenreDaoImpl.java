package ru.yandex.practicum.filmorate.storage.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Optional;

@Slf4j
@Repository
public class GenreDaoImpl implements GenreDao {
    private JdbcTemplate jdbcTemplate;

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

}
