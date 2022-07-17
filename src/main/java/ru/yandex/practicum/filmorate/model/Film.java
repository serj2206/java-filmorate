package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private String description;

    @NotNull
    private LocalDate releaseDate;

    @NotNull
    private int duration;

    private final HashSet<Long> genres = new HashSet<>();

    private final HashSet<Long> likes = new HashSet<>();

    private Mpa mpa;
}
