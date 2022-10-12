package ru.practicum.explorewithme.services.events;

import ru.practicum.explorewithme.dto.events.AdminUpdateEventRequest;
import ru.practicum.explorewithme.dto.events.EventFullDto;
import ru.practicum.explorewithme.dto.events.EventShortDto;
import ru.practicum.explorewithme.dto.events.NewEventDto;
import ru.practicum.explorewithme.dto.events.UpdateEventRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {
    List<EventShortDto> getAllEventsFiltered(EventsFilterParam filterParam);

    EventFullDto getEvent(long id, HttpServletRequest httpRequest);

    List<EventShortDto> getAllEventsForUser(long userId, long from, int size);

    EventFullDto updateEventForUser(long userId, UpdateEventRequest eventDto);

    EventFullDto createNewEventForUser(long userId, NewEventDto eventDto);

    EventFullDto getEventForUser(long eventId, long userId);

    EventFullDto cancelEventForUser(long eventId, long userId);

    List<EventFullDto> getAllEventsFilteredForAdmin(EventAdminFilterParam filterParam);

    EventFullDto updateEventForAdmin(long eventId, AdminUpdateEventRequest eventDto);

    EventFullDto publishEvent(long eventId);

    EventFullDto rejectEvent(long eventId);
}
