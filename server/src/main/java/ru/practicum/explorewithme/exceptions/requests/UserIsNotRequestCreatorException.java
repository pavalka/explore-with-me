package ru.practicum.explorewithme.exceptions.requests;

import ru.practicum.explorewithme.exceptions.OperationConditionViolationException;

public class UserIsNotRequestCreatorException extends OperationConditionViolationException {
    private static final String MSG = "Пользователь с id = %d не является создателем запроса с id = %d";

    public UserIsNotRequestCreatorException(long userId, long requestId) {
        super(String.format(MSG, userId, requestId));
    }
}
