package ru.practicum.explorewithme.exceptions.comments;

import ru.practicum.explorewithme.exceptions.DataIntegrityViolationException;

public class InvalidEventCreateTimeException extends DataIntegrityViolationException {
    private static final String MSG = "Дата начала события с id = %d еще не наступила";

    public InvalidEventCreateTimeException(long eventId) {
        super(String.format(MSG, eventId));
    }
}
