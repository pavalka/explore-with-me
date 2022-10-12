package ru.practicum.explorewithme.exceptions.events;

import ru.practicum.explorewithme.exceptions.ElementNotFoundException;

public class EventOfUserNotFoundException extends ElementNotFoundException {
    private static final String MSG = "Событие с id = %d, созданное пользователем с id= %d не найдено";

    public EventOfUserNotFoundException(long eventId, long userId) {
        super(String.format(MSG, eventId, userId));
    }
}
