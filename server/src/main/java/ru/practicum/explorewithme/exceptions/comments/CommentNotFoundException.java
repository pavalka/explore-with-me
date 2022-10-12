package ru.practicum.explorewithme.exceptions.comments;

import ru.practicum.explorewithme.exceptions.ElementNotFoundException;

public class CommentNotFoundException extends ElementNotFoundException {
    private static final String MSG = "Комментарий с id = %d не найден";

    public CommentNotFoundException(long commentId) {
        super(String.format(MSG, commentId));
    }
}
