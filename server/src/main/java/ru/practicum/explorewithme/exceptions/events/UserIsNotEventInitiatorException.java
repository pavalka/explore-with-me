package ru.practicum.explorewithme.exceptions.events;

import ru.practicum.explorewithme.exceptions.DataIntegrityViolationException;

public class UserIsNotEventInitiatorException extends DataIntegrityViolationException {
    private final static String MSG = "Пользователь с id = %d не является инициатором события с id = %d";

    public UserIsNotEventInitiatorException(long userId, long eventId) {
        super(String.format(MSG, userId, eventId));
    }
}
