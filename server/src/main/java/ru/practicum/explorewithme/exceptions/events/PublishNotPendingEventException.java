package ru.practicum.explorewithme.exceptions.events;

import ru.practicum.explorewithme.exceptions.DataIntegrityViolationException;

public class PublishNotPendingEventException extends DataIntegrityViolationException {
    private static final String MSG = "Статус события с id = %d отличен от PENDING. Публикация события невозможна";

    public PublishNotPendingEventException(long eventId) {
        super(String.format(MSG, eventId));
    }
}
