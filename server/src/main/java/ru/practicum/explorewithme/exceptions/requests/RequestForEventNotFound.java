package ru.practicum.explorewithme.exceptions.requests;

import ru.practicum.explorewithme.exceptions.ElementNotFoundException;

public class RequestForEventNotFound extends ElementNotFoundException {
    private static final String MSG = "Запрос на участие с id = %d для события с id = %d не найден";

    public RequestForEventNotFound(long requestId, long eventId) {
        super(String.format(MSG, requestId, eventId));
    }
}
