package ru.yandex.practicum.filmorate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ValidationException extends IllegalArgumentException {
    public ValidationException(final String message) {
        log.error(message);
    }

}