package ru.practicum.explorewithme.exceptions.events;

import ru.practicum.explorewithme.exceptions.ElementNotFoundException;

public class PublishedEventNotFoundException extends ElementNotFoundException {
    private static final String MSG = "Опубликованное сообщение с id = %d не найдено";

    public PublishedEventNotFoundException(long eventId) {
        super(String.format(MSG, eventId));
    }
}
