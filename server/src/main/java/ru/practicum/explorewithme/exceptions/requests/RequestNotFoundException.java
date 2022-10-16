package ru.practicum.explorewithme.exceptions.requests;

import ru.practicum.explorewithme.exceptions.ElementNotFoundException;

public class RequestNotFoundException extends ElementNotFoundException {
    private static final String MSG = "Запрос с id = %d не найден";

    public RequestNotFoundException(long requestId) {
        super(String.format(MSG, requestId));
    }
}
