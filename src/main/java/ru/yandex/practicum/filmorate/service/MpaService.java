package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.MpaNotDetectedException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDao;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
public class MpaService {
    private final MpaDao mpaDao;

    @Autowired
    public MpaService(MpaDao mpaDao) {
        this.mpaDao = mpaDao;
    }

    //Предоставить список рейтингов возрастных ограничений
    public Collection<Mpa> getMpaList() {
        log.debug(" MpaService.getMpaList()");
        return mpaDao.getList();
    }
    //Предоставить рейтинг по id
    public Mpa findMpaById(Integer id) {
        log.debug(" MpaService.findMpaById(id = {})", id);
        Optional<Mpa> optionalMpa = mpaDao.findMpaById(id);
        if (optionalMpa.isPresent()) {
            return optionalMpa.get();
        }
        else throw new MpaNotDetectedException(String.format("Рейтинг MPA c id= %s не обнаружен", id));
    }
}
