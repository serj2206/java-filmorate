package ru.yandex.practicum.filmorate.storage.filmgenre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.storage.genre.GenreDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Repository
public class FilmGenreDaoImpl implements FilmGenreDao {

    private final GenreService genreService;
    private final JdbcTemplate jdbcTemplate;
    private final GenreDao genreDao;

    public FilmGenreDaoImpl(GenreService genreService, JdbcTemplate jdbcTemplate, GenreDao genreDao) {
        this.genreService = genreService;
        this.jdbcTemplate = jdbcTemplate;
        this.genreDao = genreDao;
    }

    //Добавление
    @Override
    public void add(Set<Genre> genres, Long filmId) {
        log.debug("  FilmGenreDaoImpl.add()");
        String sqlQuery = "insert into FILM_GENRES (FILM_ID, GENRE_ID) values (?, ?)";

        if (genres != null && !genres.isEmpty()) {
            for (Genre genre : genres) {
                jdbcTemplate.update(sqlQuery, filmId, genre.getId());
            }
        }
    }

    //Обновление
    @Override
    public void update(Film film) {
        log.debug("  FilmGenreDaoImpl.update()");
        String sqlQuery = "delete from FILM_GENRES where FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
        add(film.getGenres(), film.getId());
    }

    //Выгрузка списка жанров принадлежащих фильму
    @Override
    public Collection<Genre> getFilmGenre(Long filmId) {
        log.debug("  FilmGenreDaoImpl.getFilmGenre(filmId = {})", filmId);

        String sqlQuery = "select GENRE_ID from FILM_GENRES where FILM_ID = ?";

        List<Genre> list = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeGenre(rs), filmId);

        return list;
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        Integer genreId = rs.getInt("GENRE_ID");
        return genreService.findGenreById(genreId);
    }
}
