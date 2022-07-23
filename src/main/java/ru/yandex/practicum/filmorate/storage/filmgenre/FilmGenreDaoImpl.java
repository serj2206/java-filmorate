package ru.yandex.practicum.filmorate.storage.filmgenre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Repository
public class FilmGenreDaoImpl implements FilmGenreDao {
    private final JdbcTemplate jdbcTemplate;
    private final GenreDao genreDao;

    public FilmGenreDaoImpl(JdbcTemplate jdbcTemplate, GenreDao genreDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreDao = genreDao;
    }

    //Добавление
    @Override
    public void add(Film film, Long filmId) {
        log.info("  FilmGenreDaoImpl.add()");
        String sqlQuery = "insert into FILM_GENRES (FILM_ID, GENRE_ID) values (?, ?)";
        HashSet<Genre> genres = film.getGenres();
        if (genres != null && !genres.isEmpty()) {
            for (Genre genre : genres) {
                jdbcTemplate.update(sqlQuery, filmId, genre.getId());
            }
        }
    }

    //Обновление
    @Override
    public void update(Film film) {
        log.info("  FilmGenreDaoImpl.update()");
        String sqlQuery = "delete from FILM_GENRES where FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
        add(film, film.getId());
    }

    //Выгрузка списка жанров принадлежащих фильму
    @Override
    public Collection<Genre> getFilmGenre(Film film) {
        log.info("  FilmGenreDaoImpl.getFilmGenre()");

        String sqlQuery = "select GENRE_ID from FILM_GENRES where FILM_ID = ?";

        List<Genre> list = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeGenre(rs), film.getId());

        return list;
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        Integer genreId = rs.getInt("GENRE_ID");
        Optional<Genre> optionalGenre = genreDao.findGenreById(genreId);
        if (optionalGenre.isPresent()) {
            return optionalGenre.get();
        }
        Genre genre = new Genre();
        genre.setId(genreId);
        return genre;
    }
}
