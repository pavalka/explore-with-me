package ru.practicum.explorewithme.exceptions.requests;

import ru.practicum.explorewithme.exceptions.OperationConditionViolationException;

public class UserIsEventInitiatorException extends OperationConditionViolationException {
    private static final String MSG = "Пользователь с id = %d  является инициатором события с id = %d";

    public UserIsEventInitiatorException(long userId, long eventId) {
        super(String.format(MSG, userId, eventId));
    }
}
