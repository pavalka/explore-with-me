package ru.practicum.explorewithme.services.events;

import ru.practicum.explorewithme.entities.categories.Category;
import ru.practicum.explorewithme.entities.events.Event;
import ru.practicum.explorewithme.entities.users.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ThirdPartEventService {
    Event getEventById(long eventId);

    Event getEventByIdAndUser(long eventId, User user);

    List<Event> getEventsById(Collection<Long> events);

    long getStatisticsByEvent(Event event);

    Map<Long, Long> getStatisticsByEvents(Collection<Event> events);

    boolean isCategoryUsed(Category category);

    Event getPublishedEventById(long eventId);
}
