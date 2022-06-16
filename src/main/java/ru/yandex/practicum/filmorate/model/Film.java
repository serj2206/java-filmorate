package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder(toBuilder = true)
public class Film {
    /*
    целочисленный идентификатор — id;
    название — name;
    описание — description;
    дата релиза — releaseDate;
    продолжительность фильма — duration.
    */
    private int id;

    @NotBlank
    private String name;

    @NotNull
    private String description;

    @NotNull
    private LocalDate releaseDate;

    @NotNull
    private int duration;

    private HashSet<Integer> likes;
    //Добавление лайка от пользователя с id = idUser
    public boolean addLike(Integer idUser) {
        likes.add(idUser);
        return true;
    }
    //Удаление лайка от пользователя с id = idUser
    public boolean deleteLike(Integer idUser) {
        if(likes.contains(idUser)) {
            likes.remove(idUser);
            return true;
        }
        return false;
    }

    //Предоставление списка пользователей поставивших лайк
    public List<Integer> getLike() {
        return new ArrayList<>(likes);
    }

}
