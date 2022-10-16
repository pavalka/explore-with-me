package ru.practicum.explorewithme.exceptions.comments;

import ru.practicum.explorewithme.exceptions.OperationConditionViolationException;

public class UpdatingModeratedCommentException extends OperationConditionViolationException {
    private static final String MSG = "Редактирование комментария c id = %d, который прошел модерацию. Такое " +
            "редактирование запрещено";

    public UpdatingModeratedCommentException(long commentId) {
        super(String.format(MSG, commentId));
    }
}
