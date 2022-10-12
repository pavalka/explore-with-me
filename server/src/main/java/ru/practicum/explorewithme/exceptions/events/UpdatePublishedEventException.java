package ru.practicum.explorewithme.exceptions.events;

import ru.practicum.explorewithme.exceptions.OperationConditionViolationException;

public class UpdatePublishedEventException extends OperationConditionViolationException {
    private static final String MSG = "Попытка изменить событие (id = %d) со статусом PUBLISHED или REJECTED";

    public UpdatePublishedEventException(long eventId) {
        super(String.format(MSG, eventId));
    }
}
