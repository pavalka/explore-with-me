package ru.practicum.explorewithme.exceptions.users;

import ru.practicum.explorewithme.exceptions.ElementNotFoundException;

public class UserNotFoundException extends ElementNotFoundException {
    private static final String msg = "Пользователь с id = %d не найден.";

    public UserNotFoundException(long userId) {
        super(String.format(msg, userId));
    }
}
