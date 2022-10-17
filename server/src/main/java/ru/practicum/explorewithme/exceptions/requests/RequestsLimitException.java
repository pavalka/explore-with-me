package ru.practicum.explorewithme.exceptions.requests;

import ru.practicum.explorewithme.exceptions.OperationConditionViolationException;

public class RequestsLimitException extends OperationConditionViolationException {
    private static final String MSG = "Для события с id = %d достигнут лимит запросов на участие";

    public RequestsLimitException(long eventId) {
        super(String.format(MSG, eventId));
    }
}
