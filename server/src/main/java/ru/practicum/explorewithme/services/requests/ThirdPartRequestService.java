package ru.practicum.explorewithme.services.requests;

import ru.practicum.explorewithme.entities.events.Event;
import ru.practicum.explorewithme.entities.requests.Request;
import ru.practicum.explorewithme.entities.users.User;

import java.util.Collection;
import java.util.Map;

public interface ThirdPartRequestService {
    Map<Long, Long> getConfirmedRequests(Collection<Event> events);

    long getConfirmedRequest(Event event);

    Request getRequestById(long requestId);

    boolean isExistConfirmedRequestForUserAndEvent(User user, Event event);
}
