package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {
    @Autowired
    private final MpaService mpaService;

    @GetMapping("/{id}")
    public Mpa get(@PathVariable("id") int id) {
        return mpaService.getMpaById(id);
    }

    @ResponseBody
    @GetMapping
    public List<Mpa> getMpa() {
        return mpaService.getMpa();
    }
}
