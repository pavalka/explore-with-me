package ru.practicum.explorewithme.exceptions.comments;

import ru.practicum.explorewithme.exceptions.DataIntegrityViolationException;

public class ApprovingNonPendingCommentException extends DataIntegrityViolationException {
    private static final String MSG = "Подтверждение комментария с id = %d, статус которого отличен от PENDING. " +
            "Данная операция недопустима";

    public ApprovingNonPendingCommentException(long commentId) {
        super(String.format(MSG, commentId));
    }
}
