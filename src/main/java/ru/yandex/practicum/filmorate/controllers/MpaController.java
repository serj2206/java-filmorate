package ru.yandex.practicum.filmorate.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import javax.validation.ValidationException;
import java.util.Collection;

@Slf4j
@RestController
public class MpaController {
    public final MpaService mpaService;

    @Autowired
    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }


    @GetMapping("/mpa")
    public Collection<Mpa> getMpaList() {
        log.info("Get-запрос на список рейтингов MPA");
        return mpaService.getMpaList();
    }

    @GetMapping("/mpa/{id}")
    public Mpa getMpa(@PathVariable Integer id) {
        log.info("Get-запрос рейтингf MPA по ID");
         return mpaService.findMpaById(id);
    }
}

