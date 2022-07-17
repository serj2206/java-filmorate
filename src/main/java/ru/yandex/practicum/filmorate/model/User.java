package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.*;

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


    private Long id;

    @NotNull
    @Email
    private String email;

    @Pattern(regexp = "^\\S*$")
    //@NotBlank
    private String login;

    private String name;

    @NotNull
    private LocalDate birthday;

    private final Set<Long> friends =new HashSet<>();


    public User(Long id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}
