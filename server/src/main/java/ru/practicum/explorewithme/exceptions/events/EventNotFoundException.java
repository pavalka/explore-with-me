package ru.practicum.explorewithme.exceptions.events;

import ru.practicum.explorewithme.exceptions.ElementNotFoundException;

public class EventNotFoundException extends ElementNotFoundException {
    private static final String MSG = "Событие с id = %d не найдено";

    public EventNotFoundException(long eventId) {
        super(String.format(MSG, eventId));
    }
}
