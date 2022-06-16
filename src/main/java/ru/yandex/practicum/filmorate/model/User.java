package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder(toBuilder = true)
public class User {
    /*
    целочисленный идентификатор — id;
    электронная почта — email;
    логин пользователя — login;
    имя для отображения — name;
    дата рождения — birthday.
    */
    private int id;

    @NotNull
    @Email
    private String email;

    @Pattern(regexp = "^\\S*$")
    //@NotBlank
    private String login;

    private String name;

    @NotNull
    private LocalDate birthday;

    private Set<Integer> friends = new HashSet<>();

    //Добавить пользователя в список друзей
    public boolean addFriend(Integer id) {
        friends.add(id);
        return true;
    }
    //Удалить пользователя из списка друзей
    public boolean deleteFriend(Integer id) {
        if (friends.contains(id)){
            friends.remove(id);
            return true;
        }
        return false;
    }

    //Дать список друзей
    public List<Integer> getFriends() {
        return friends.stream().collect(Collectors.toList());
    }

}
