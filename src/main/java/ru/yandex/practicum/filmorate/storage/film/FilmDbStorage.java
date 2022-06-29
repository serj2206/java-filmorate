package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.MpaNotDetectedException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.filmgenre.FilmGenreDao;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@Component("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmGenreDao filmGenreDao;
    private final MpaDao mpaDao;



    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, FilmGenreDao filmGenreDao, MpaDao mpaDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmGenreDao = filmGenreDao;
        this.mpaDao = mpaDao;
    }

    //Вернуть фильм по id
    @Override
    public Optional<Film> findFilmById(Long id) {
        log.debug("  FilmDbStorage.findFilmById(id = {})", id);
        Film film;
        Mpa mpa;
        String sqlQuery = "select * from FILMS where FILM_ID = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (filmRows.next()) {
            film = Film.builder()
                    .id(filmRows.getLong("FILM_ID"))
                    .name(filmRows.getString("FILM_NAME"))
                    .description(filmRows.getString("FILM_DESCRIPTION"))
                    .duration(filmRows.getInt("DURATION"))
                    .releaseDate(filmRows.getDate("RELEASE_DATA").toLocalDate())
                    .build();
            Optional<Mpa> optionalMpa = mpaDao.findMpaById(filmRows.getInt("MPA_ID"));
            if(optionalMpa.isPresent()) {
                mpa = optionalMpa.get();
                film.setMpa(mpa);
            } else throw new MpaNotDetectedException(String.format("Рейтинг MPA c id= %s не обнаружен", filmRows.getInt("MPA_ID")));
            return Optional.of(film);
        }
        return Optional.empty();
    }

    @Override
    public Long add(Film film) {
        log.debug("  FilmDbStorage.add()");
        String sqlQuery = "insert into Films (FILM_NAME, FILM_DESCRIPTION, DURATION, RELEASE_DATA, MPA_ID)" +
                "values (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setInt(3, film.getDuration());
            stmt.setString(4, film.getReleaseDate().toString());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        Long filmId = keyHolder.getKey().longValue();

        return filmId;
    }

    @Override
    public boolean delete(Long id) {
        log.debug("  FilmDbStorage.delete(id = {})", id);
        String sqlQuery = "delete from FILMS where FILM_ID = ?";
        if (jdbcTemplate.update(sqlQuery, id) == 1) return true;
        return false;
    }

    @Override
    public boolean update(Film film) {
        log.debug("  FilmDbStorage.update()");
        String sqlQuery = "update FILMS set FILM_NAME = ?, FILM_DESCRIPTION = ?, RELEASE_DATA  = ?, DURATION = ?," +
                "MPA_ID = ? where FILM_ID = ?";
        if (jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate().toString(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()) > 0) return true;
        else return false;
    }

    @Override
    public Collection<Film> getFilms() {
        log.debug("  FilmDbStorage.getFilms()");
        String sqlQuery = "select * from FILMS";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs));
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        Mpa mpa;
        Film film = Film.builder()
                .id(rs.getLong("FILM_ID"))
                .name(rs.getString("FILM_NAME"))
                .description(rs.getString("FILM_DESCRIPTION"))
                .duration(rs.getInt("DURATION"))
                .releaseDate(rs.getDate("RELEASE_DATA").toLocalDate())
                .build();
        Optional<Mpa> optionalMpa = mpaDao.findMpaById(rs.getInt("MPA_ID"));
        if(optionalMpa.isPresent()) {
            mpa = optionalMpa.get();
            film.setMpa(mpa);
        } else throw new MpaNotDetectedException(String.format("Рейтинг MPA c id= %s не обнаружен", rs.getInt("MPA_ID")));
        return film;
    }


}
