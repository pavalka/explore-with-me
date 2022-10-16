package ru.practicum.explorewithme.exceptions.events;

import ru.practicum.explorewithme.exceptions.DataIntegrityViolationException;

public class RejectNotPendingException extends DataIntegrityViolationException {
    private static final String MSG = "Статус события с id = %d отличен от PENDING. Отклонить событие невозможно";

    public RejectNotPendingException(long eventId) {
        super(String.format(MSG, eventId));
    }
}
