package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

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

}
