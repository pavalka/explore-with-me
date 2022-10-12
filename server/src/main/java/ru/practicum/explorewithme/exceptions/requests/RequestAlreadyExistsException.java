package ru.practicum.explorewithme.exceptions.requests;

import ru.practicum.explorewithme.exceptions.OperationConditionViolationException;

public class RequestAlreadyExistsException extends OperationConditionViolationException {
    private static final String MSG = "Запрос пользователя с id = %d для события с id = %d уже существует";

    public RequestAlreadyExistsException(long userId, long eventId) {
        super(String.format(MSG, userId, eventId));
    }
}
