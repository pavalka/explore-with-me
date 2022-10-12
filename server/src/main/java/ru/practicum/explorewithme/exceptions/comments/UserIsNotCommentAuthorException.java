package ru.practicum.explorewithme.exceptions.comments;

import ru.practicum.explorewithme.exceptions.DataIntegrityViolationException;

public class UserIsNotCommentAuthorException extends DataIntegrityViolationException {
    private static final String MSG = "Пользователь с id = %d не является автором комментария с id = %d";

    public UserIsNotCommentAuthorException(long userId, long commentId) {
        super(String.format(MSG, userId, commentId));
    }
}
