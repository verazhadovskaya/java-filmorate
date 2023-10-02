package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaService {
    @Qualifier("mpaDbStorage")
    private final MpaDbStorage mpaDbStorage;

    public Mpa getMpaById(int id) {
        return mpaDbStorage.getMpaById(id);
    }

    public List<Mpa> getMpa() {
        return mpaDbStorage.getAllMpa();
    }
}
