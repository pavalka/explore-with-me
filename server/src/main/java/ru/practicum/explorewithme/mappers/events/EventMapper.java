package ru.practicum.explorewithme.mappers.events;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ru.practicum.explorewithme.dto.events.EventFullDto;
import ru.practicum.explorewithme.dto.events.EventShortDto;
import ru.practicum.explorewithme.dto.events.NewEventDto;
import ru.practicum.explorewithme.entities.categories.Category;
import ru.practicum.explorewithme.entities.events.Event;
import ru.practicum.explorewithme.entities.users.User;
import ru.practicum.explorewithme.mappers.categories.CategoryMapper;
import ru.practicum.explorewithme.mappers.location.LocationMapper;
import ru.practicum.explorewithme.mappers.users.UserMapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {
    public static EventFullDto mapToFullDto(Event event, long confirmedRequests, long views) {
        if (event == null) {
            return null;
        }

        var eventDto = new EventFullDto();

        eventDto.setId(event.getId());
        eventDto.setTitle(event.getTitle());
        eventDto.setDescription(event.getDescription());
        eventDto.setAnnotation(event.getAnnotation());
        eventDto.setCategory(CategoryMapper
                        .mapToDto(event.getCategory()));
        eventDto.setInitiator(UserMapper
                        .mapToShortDto(event.getInitiator()));
        eventDto.setCreatedOn(event.getCreatedOn());
        eventDto.setEventDate(event.getEventDate());
        eventDto.setPublishedOn(event.getPublishedOn());
        eventDto.setLocation(LocationMapper.mapToDto(event.getLocation()));
        eventDto.setPaid(event.getPaid());
        eventDto.setRequestModeration(event.getRequestModeration());
        eventDto.setState(event.getState());
        eventDto.setParticipantLimit(event.getParticipantsLimit());
        eventDto.setConfirmedRequests(confirmedRequests);
        eventDto.setViews(views);
        return eventDto;
    }

    public static EventShortDto mapToShortDto(Event event, long confirmedRequests, long views) {
        if (event == null) {
            return null;
        }

        var eventDto = new EventShortDto();

        eventDto.setId(event.getId());
        eventDto.setTitle(event.getTitle());
        eventDto.setAnnotation(event.getAnnotation());
        eventDto.setCategory(CategoryMapper
                        .mapToDto(event.getCategory()));
        eventDto.setInitiator(UserMapper
                        .mapToShortDto(event.getInitiator()));
        eventDto.setEventDate(event.getEventDate());
        eventDto.setPaid(event.getPaid());
        eventDto.setConfirmedRequests(confirmedRequests);
        eventDto.setViews(views);
        return eventDto;
    }

    public static List<EventShortDto> mapToShortDto(Collection<Event> events, @NonNull Map<Long, Long> confirmedRequests,
                                                    @NonNull Map<Long, Long> views) {
        if (events == null) {
            return null;
        }
        return events.stream()
                .map(event -> EventMapper.mapToShortDto(event, confirmedRequests.getOrDefault(event.getId(), 0L),
                            views.getOrDefault(event.getId(), 0L)))
                .collect(Collectors.toList());
    }

    public static List<EventFullDto> mapToFullDto(Collection<Event> events, @NonNull Map<Long, Long> confirmedRequests,
                                                    @NonNull Map<Long, Long> views) {
        if (events == null) {
            return null;
        }
        return events.stream()
                .map(event -> EventMapper.mapToFullDto(event, confirmedRequests.getOrDefault(event.getId(), 0L),
                            views.getOrDefault(event.getId(), 0L)))
                .collect(Collectors.toList());
    }

    public static Event mapFromDto(NewEventDto eventDto, User user, Category category) {
        if (eventDto == null) {
            return null;
        }

        var event = new Event();

        event.setTitle(eventDto.getTitle());
        event.setAnnotation(eventDto.getAnnotation());
        event.setDescription(eventDto.getDescription());
        event.setCategory(category);
        event.setInitiator(user);
        event.setLocation(LocationMapper
                .mapFromDto(eventDto.getLocation()));
        event.setEventDate(eventDto.getEventDate());
        event.setPaid(eventDto.isPaid());
        event.setParticipantsLimit(eventDto.getParticipantLimit());
        event.setRequestModeration(eventDto.isRequestModeration());
        return event;
    }
}
