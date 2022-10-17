package ru.practicum.explorewithme.exceptions.requests;

import ru.practicum.explorewithme.exceptions.OperationConditionViolationException;

public class UserIsNotEventInitiator extends OperationConditionViolationException {
    private static final String MSG = "Пользователь с id = %d не является инициатором события с id= %d";

    public UserIsNotEventInitiator(long userId, long eventId) {
        super(String.format(MSG, userId, eventId));
    }
}
