package ru.practicum.explorewithme.exceptions;

public class EventIsNotPublishedException extends OperationConditionViolationException {
    private static final String MSG = "Событие с id = %d не опубликовано";

    public EventIsNotPublishedException(long eventId) {
        super(String.format(MSG, eventId));
    }
}
