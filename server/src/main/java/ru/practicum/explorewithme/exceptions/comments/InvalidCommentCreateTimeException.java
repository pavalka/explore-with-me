package ru.practicum.explorewithme.exceptions.comments;

import ru.practicum.explorewithme.exceptions.OperationConditionViolationException;

public class InvalidCommentCreateTimeException extends OperationConditionViolationException {
    private static final String MSG = "От начала события с id = %d прошло менее 1 часа.";

    public InvalidCommentCreateTimeException(long eventId) {
        super(String.format(MSG, eventId));
    }
}
