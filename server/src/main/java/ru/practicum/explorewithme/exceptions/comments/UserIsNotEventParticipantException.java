package ru.practicum.explorewithme.exceptions.comments;

import ru.practicum.explorewithme.exceptions.DataIntegrityViolationException;

public class UserIsNotEventParticipantException extends DataIntegrityViolationException {
    private static final String MSG = "Пользователь с id = %d не является участником события с id = %d";

    public UserIsNotEventParticipantException(long userId, long eventId) {
        super(String.format(MSG, userId, eventId));
    }
}
