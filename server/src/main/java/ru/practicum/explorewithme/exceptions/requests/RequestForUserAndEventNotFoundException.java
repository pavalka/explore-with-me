package ru.practicum.explorewithme.exceptions.requests;

import ru.practicum.explorewithme.exceptions.ElementNotFoundException;

public class RequestForUserAndEventNotFoundException extends ElementNotFoundException {
    private static final String MSG = "Подтвержденный запрос для пользователя с id = %d на участие в событии с " +
            "id = %d не найден";

    public RequestForUserAndEventNotFoundException(long userId, long eventId) {
        super(String.format(MSG, userId, eventId));
    }
}
