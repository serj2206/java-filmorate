package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.filmgenre.FilmGenreDao;
import ru.yandex.practicum.filmorate.storage.friendship.FriendShipDao;
import ru.yandex.practicum.filmorate.storage.genre.GenreDao;
import ru.yandex.practicum.filmorate.storage.like.LikeDao;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDao;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FilmorateApplicationTests {
	private final UserDbStorage userStorage;
	private final FilmDbStorage filmStorage;
	private final FilmGenreDao filmGenreDao;
	private final MpaDao mpaDao;
	private final GenreDao genreDao;
	private final LikeDao likeDao;
	private final FriendShipDao friendShipDao;

//Блок User
	@Test
	@Order(1)
	public void testAddUser(){
		log.debug("Test 1");
		User userTest = User.builder()
				.email("email@yandex.ru")
				.name("Petr")
				.birthday(LocalDate.of(1990, 10, 11))
				.login("Qron")
				.build();
		Long id = userStorage.add(userTest);
		assertEquals(id, 1L);

	}

	@Test
	@Order(2)
	public void testFindUserById() {
		log.debug("Test 2");
		Optional<User> userOptional = userStorage.findUserById(1L);

		assertThat(userOptional)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user)
								.hasFieldOrPropertyWithValue("id", 1L)
								.hasFieldOrPropertyWithValue("name", "Petr")
								.hasFieldOrPropertyWithValue("birthday", LocalDate.of(1990, 10, 11))
								.hasFieldOrPropertyWithValue("login", "Qron")
				);
	}

	@Test
	@Order(3)
	public void testUpdateUser() {
		log.debug("Test 3");
		User userTest = User.builder()
				.id(1L)
				.email("update@yandex.ru")
				.name("Petr_update")
				.birthday(LocalDate.of(1991, 11, 12))
				.login("update")
				.build();
		userStorage.update(userTest);
		Optional<User> userOptional = userStorage.findUserById(1L);
		assertThat(userOptional)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user)
								.hasFieldOrPropertyWithValue("id", 1L)
								.hasFieldOrPropertyWithValue("name", "Petr_update")
								.hasFieldOrPropertyWithValue("birthday", LocalDate.of(1991, 11, 12))
								.hasFieldOrPropertyWithValue("login", "update")
				);

	}

	@Test
	@Order(4)
	public void testDeleteUser() {
		log.debug("Test 4");
		assertTrue(userStorage.delete(1L));

	}

	//Блок Film
	@Test
	@Order(5)
	public void testAddFilm() {;
		log.debug("Test 5");
		Mpa mpa1 = new Mpa();
		mpa1.setId(1);
		Film filmTest = Film.builder()
				.name("Полет")
				.description("Описание")
				.duration(50)
				.releaseDate(LocalDate.of(1950, 5, 25))
				.mpa(mpa1)
				.build();
		assertEquals(1, filmStorage.add(filmTest));
	}

	@Test
	@Order(6)
	public void testFindFilmById() {
		log.debug("Test 6");
		Optional<Film> filmOptional = filmStorage.findFilmById(1L);
		assertThat(filmOptional)
				.isPresent()
				.hasValueSatisfying(film ->
						assertThat(film)
								.hasFieldOrPropertyWithValue("id", 1L)
								.hasFieldOrPropertyWithValue("name", "Полет")
								.hasFieldOrPropertyWithValue("duration", 50)
								.hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(1950, 5, 25))
								.hasFieldOrPropertyWithValue("mpa", new Mpa(1, "G"))
				);
	}

	@Test
	@Order(7)
	public void testUpdateFilm() {
		log.debug("Test 7");
		Mpa mpa1 = new Mpa();
		mpa1.setId(2);
		Film filmTest = Film.builder()
				.id(1L)
				.name("ПолетИзм")
				.description("ОписаниеИзм")
				.duration(51)
				.releaseDate(LocalDate.of(1950, 5, 26))
				.mpa(mpa1)
				.build();
		assertTrue(filmStorage.update(filmTest));
		Optional<Film> filmOptional = filmStorage.findFilmById(1L);
		assertThat(filmOptional)
				.isPresent()
				.hasValueSatisfying(film ->
						assertThat(film)
								.hasFieldOrPropertyWithValue("id", 1L)
								.hasFieldOrPropertyWithValue("name", "ПолетИзм")
								.hasFieldOrPropertyWithValue("duration", 51)
								.hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(1950, 5, 26))
								.hasFieldOrPropertyWithValue("mpa", new Mpa(2, "PG"))
				);
	}


	@Test
	@Order(8)
	public void testAddFilmGenre() {
		log.debug("Test 8");
		Long filmId = 1L;

		Genre genre1 = new Genre(1, "Комедия");
		Genre genre2 = new Genre(2, "Драма");
		Set<Genre> genres = new HashSet<>();
		genres.add(genre1);
		genres.add(genre2);

		filmGenreDao.add(genres, filmId);

		List<Genre> genreList = new ArrayList<>(filmGenreDao.getFilmGenre(filmId));
		assertTrue(genreList.contains(genre1));
		assertTrue(genreList.contains(genre2));
		assertTrue(genreList.size() == 2);
	}

	@Test
	@Order(9)
	public void testDeleteFilm() {
		log.debug("Test 9");
		Long filmId = 1L;

		assertTrue(filmStorage.delete(filmId));

		assertTrue(filmGenreDao.getFilmGenre(filmId).isEmpty());
	}

	//Блок Mpa
	@Test
	@Order(10)
	public void testfindMpaById() {
		log.debug("Test 10");
		Optional<Mpa> optionalMpa = mpaDao.findMpaById(1);
		assertThat(optionalMpa)
				.isPresent()
				.hasValueSatisfying(mpa ->
						assertThat(mpa)
								.hasFieldOrPropertyWithValue("id", 1)
								.hasFieldOrPropertyWithValue("name", "G")
				);
	}
	@Test
	@Order(11)
	public void testGetListMpa() {
		log.debug("Test 11");
		List<Mpa> mpaList = new ArrayList<>(mpaDao.getList());
		assertTrue(mpaList.size() == 5);
		assertTrue(mpaList.contains(new Mpa(1, "G")));
		assertTrue(mpaList.contains(new Mpa(2, "PG")));
		assertTrue(mpaList.contains(new Mpa(3, "PG-13")));
		assertTrue(mpaList.contains(new Mpa(4, "R")));
		assertTrue(mpaList.contains(new Mpa(5, "NC-17")));
	}

	//Блок Genre
	@Test
	@Order(12)
	public void testFindGenreById() {
		log.debug("Test 12");
		Optional<Genre> optionalGenre = genreDao.findGenreById(1);
		assertThat(optionalGenre)
				.isPresent()
				.hasValueSatisfying(genre ->
						assertThat(genre)
								.hasFieldOrPropertyWithValue("id", 1)
								.hasFieldOrPropertyWithValue("name", "Комедия")
				);

	}

	@Test
	@Order(13)
	public void testgetListGenre() {
		log.debug("Test 13");
		List<Genre> genreList = new ArrayList<>(genreDao.getList());
		assertTrue(genreList.size() == 6);
		assertTrue(genreList.contains(new Genre(1, "Комедия")));
		assertTrue(genreList.contains(new Genre(2, "Драма")));
		assertTrue(genreList.contains(new Genre(3, "Мультфильм")));
		assertTrue(genreList.contains(new Genre(4, "Триллер")));
		assertTrue(genreList.contains(new Genre(5, "Документальный")));
		assertTrue(genreList.contains(new Genre(6, "Боевик")));
	}


	//Блок Like
	@Test
	@Order(14)
	public void testAddLike() {
		log.debug("Test 14");
		User user1 = User.builder()
				.email("email@yandex.ru")
				.name("Petr")
				.birthday(LocalDate.of(1990, 10, 11))
				.login("Qron")
				.build();
		Long userId1 = userStorage.add(user1);

		Mpa mpa1 = new Mpa();
		mpa1.setId(1);
		Film film1 = Film.builder()
				.name("Полет")
				.description("Описание")
				.duration(50)
				.releaseDate(LocalDate.of(1950, 5, 25))
				.mpa(mpa1)
				.build();
		Long filmId1 = filmStorage.add(film1);

		assertTrue(likeDao.add(filmId1, userId1));
	}

	@Test
	@Order(15)
	public void testGetTop() {
		log.debug("Test 15");
		Long userId1 = 2L;
		Long filmId1 =2L;
		User user2 = User.builder()
				.email("email_Igor@yandex.ru")
				.name("Igor")
				.birthday(LocalDate.of(1980, 11, 15))
				.login("Lampa")
				.build();
		Long userId2 = userStorage.add(user2);

		Mpa mpa1 = new Mpa();
		mpa1.setId(1);
		Film film2 = Film.builder()
				.name("Кораблестроение")
				.description("Описание2")
				.duration(120)
				.releaseDate(LocalDate.of(1960, 6, 26))
				.mpa(mpa1)
				.build();
		Long filmId2 = filmStorage.add(film2);

		User user3 = User.builder()
				.email("email_Dima@yandex.ru")
				.name("Dima")
				.birthday(LocalDate.of(1981, 12, 1))
				.login("Struna")
				.build();
		Long userId3 = userStorage.add(user3);


		mpa1.setId(1);
		Film film3 = Film.builder()
				.name("Самолетостроение")
				.description("Описание3")
				.duration(110)
				.releaseDate(LocalDate.of(1955, 2, 16))
				.mpa(mpa1)
				.build();
		Long filmId3 = filmStorage.add(film3);
		likeDao.add(filmId1,userId2);
		likeDao.add(filmId3,userId3);
		likeDao.add(filmId1,userId3);

		List<Long> top =new ArrayList<>(likeDao.getTop(10));
		assertTrue(top.get(0) == filmId1);
		assertTrue(top.get(1) == filmId3);
		assertTrue(top.get(2) == filmId2);


	}

	@Test
	@Order(16)
	public void testDeleteLike() {
		log.debug("Test 16");
		assertTrue(likeDao.delete(2L,2L));
	}

	//Блок friendship
	@Test
	@Order(17)
	public void testAddFriend() {
		log.debug("Test 17");
		assertTrue(friendShipDao.add(2L,3L));
	}

	@Test
	@Order(18)
	public void testGetListFriend() {
		log.debug("Test 18");
		friendShipDao.add(2L,4L);
		List<Long> list = new ArrayList<>(friendShipDao.getList(2L));
		assertTrue(list.size() == 2);
		assertTrue(list.contains(3L));
		assertTrue(list.contains(4L));
	}

	@Test
	@Order(19)
	public void testGetCommonFriendList() {
		log.debug("Test 19");
		friendShipDao.add(3L,2L);
		friendShipDao.add(3L, 4L);
		friendShipDao.add(4L, 2L);
		List<Long> list = new ArrayList<>(friendShipDao.getCommonFriends(2L, 3L));
		assertTrue(list.size() == 1);
		assertTrue(list.contains(4L));
	}

	@Test
	@Order(20)
	public void testDeleteFriend() {
		log.debug("Test 20");
		assertTrue(friendShipDao.delete(2L, 3L));
	}

	@Test
	void contextLoads() {
	}

}
