package ru.practicum.explorewithme.exceptions.comments;

import ru.practicum.explorewithme.exceptions.ElementNotFoundException;

public class CommentOfUserNotFoundException extends ElementNotFoundException {
    private static final String MSG = "Комментария с id = %d, созданный пользователем с id = %d не найден";

    public CommentOfUserNotFoundException(long commentId, long userId) {
        super(String.format(MSG, commentId, userId));
    }
}
