package ru.practicum.explorewithme.services.events;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.events.AdminUpdateEventRequest;
import ru.practicum.explorewithme.dto.events.EventFullDto;
import ru.practicum.explorewithme.dto.events.EventShortDto;
import ru.practicum.explorewithme.dto.events.NewEventDto;
import ru.practicum.explorewithme.dto.events.UpdateEventRequest;
import ru.practicum.explorewithme.dto.statistics.ViewStats;
import ru.practicum.explorewithme.entities.categories.Category;
import ru.practicum.explorewithme.entities.events.Event;
import ru.practicum.explorewithme.entities.events.EventState;
import ru.practicum.explorewithme.entities.users.User;
import ru.practicum.explorewithme.exceptions.EventIsNotPublishedException;
import ru.practicum.explorewithme.exceptions.events.CancelingPublishedEventException;
import ru.practicum.explorewithme.exceptions.events.EventNotFoundException;
import ru.practicum.explorewithme.exceptions.events.EventOfUserNotFoundException;
import ru.practicum.explorewithme.exceptions.events.IncorrectTimeShiftException;
import ru.practicum.explorewithme.exceptions.events.PublishNotPendingEventException;
import ru.practicum.explorewithme.exceptions.events.PublishedEventNotFoundException;
import ru.practicum.explorewithme.exceptions.events.RejectNotPendingException;
import ru.practicum.explorewithme.exceptions.events.UpdatePublishedEventException;
import ru.practicum.explorewithme.exceptions.events.UserIsNotEventInitiatorException;
import ru.practicum.explorewithme.mappers.events.EventMapper;
import ru.practicum.explorewithme.mappers.location.LocationMapper;
import ru.practicum.explorewithme.pageable.PageableByOffsetAndSize;
import ru.practicum.explorewithme.repositories.events.EventRepository;
import ru.practicum.explorewithme.repositories.events.SearchingBetweenStartAndEndDates;
import ru.practicum.explorewithme.repositories.events.SearchingByAnnotationOrDescription;
import ru.practicum.explorewithme.repositories.events.SearchingByAvailable;
import ru.practicum.explorewithme.repositories.events.SearchingByCategories;
import ru.practicum.explorewithme.repositories.events.SearchingByListOfStates;
import ru.practicum.explorewithme.repositories.events.SearchingByPaid;
import ru.practicum.explorewithme.repositories.events.SearchingByRangeEnd;
import ru.practicum.explorewithme.repositories.events.SearchingByRangeStart;
import ru.practicum.explorewithme.repositories.events.SearchingByState;
import ru.practicum.explorewithme.repositories.events.SearchingByUsers;
import ru.practicum.explorewithme.services.categories.ThirdPartCategoryService;
import ru.practicum.explorewithme.services.requests.ThirdPartRequestService;
import ru.practicum.explorewithme.services.users.ThirdPartUserService;
import ru.practicum.explorewithme.statisticsclient.StatisticsClient;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@Slf4j
public class EventServiceImpl implements EventService, ThirdPartEventService {
    private static final long TIME_SHIFT_2_HOURS = 2;
    private static final long TIME_SHIFT_1_HOUR = 1;
    private static final String EVENT_BASE_URI = "/events";
    private static final long SHIFT_500_YEARS = 500;

    private final EventRepository eventRepository;
    private final ThirdPartUserService userService;
    private final ThirdPartRequestService requestService;
    private final StatisticsClient statisticsClient;
    private final ThirdPartCategoryService categoryService;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository, ThirdPartUserService userService,
                            StatisticsClient statisticsClient, @Lazy ThirdPartCategoryService categoryService,
                            @Lazy ThirdPartRequestService requestService) {
        this.eventRepository = eventRepository;
        this.userService = userService;
        this.requestService = requestService;
        this.statisticsClient = statisticsClient;
        this.categoryService = categoryService;
    }

    @Override
    public List<EventShortDto> getAllEventsFiltered(EventsFilterParam filterParam) {
        Specification<Event> constraints = createOrAddConstraint(null, new SearchingByState(EventState.PUBLISHED));

        if (filterParam.getText() != null) {
            constraints = createOrAddConstraint(constraints, new SearchingByAnnotationOrDescription(filterParam.getText()));
        }
        if (filterParam.getCategories() != null) {
            constraints = createOrAddConstraint(constraints, new SearchingByCategories(filterParam.getCategories()));
        }
        if (filterParam.getPaid() != null) {
            constraints = createOrAddConstraint(constraints, new SearchingByPaid(filterParam.getPaid()));
        }
        if (filterParam.getRangeStart() != null && filterParam.getRangeEnd() != null) {
            constraints = createOrAddConstraint(constraints, new SearchingBetweenStartAndEndDates(filterParam.getRangeStart(),
                    filterParam.getRangeEnd()));
        } else {
            constraints = createOrAddConstraint(constraints, new SearchingByRangeStart(LocalDateTime.now()));
        }
        if (filterParam.getOnlyAvailable() != null) {
            constraints = createOrAddConstraint(constraints, new SearchingByAvailable());
        }

        var events = eventRepository.findAll(constraints,
                new PageableByOffsetAndSize(filterParam.getFrom(), filterParam.getSize())).toList();
        var confirmedRequests = requestService.getConfirmedRequests(events);
        var eventViews = getStatisticsByEvents(events);

        var eventsDto = EventMapper.mapToShortDto(events, confirmedRequests, eventViews);

        statisticsClient.sendStatistics("/events", filterParam.getRequest().getRemoteAddr());
        switch (filterParam.getSort()) {
            case VIEWS:
                return eventsDto.stream().sorted(Comparator.comparing(EventShortDto::getViews, Long::compare))
                        .collect(Collectors.toList());

            case EVENT_DATE:
                return eventsDto.stream().sorted(Comparator.comparing(EventShortDto::getEventDate))
                        .collect(Collectors.toList());
        }
        return eventsDto;
    }

    @Override
    public EventFullDto getEvent(long id, HttpServletRequest httpRequest) {
        var event = getEventById(id);

        if (event.getState() != EventState.PUBLISHED) {
            throw new EventIsNotPublishedException(id);
        }

        statisticsClient.sendStatistics(String.format("/events/%d", id), httpRequest.getRemoteAddr());
        return EventMapper.mapToFullDto(event, requestService.getConfirmedRequest(event),
                getStatisticsByEvent(event));
    }

    @Override
    public List<EventShortDto> getAllEventsForUser(long userId, long from, int size) {
        var user = userService.getUser(userId);
        var events = eventRepository.findByInitiator(user, new PageableByOffsetAndSize(from, size));

        return EventMapper.mapToShortDto(events, requestService.getConfirmedRequests(events),
                getStatisticsByEvents(events));
    }

    @Override
    @Transactional
    public EventFullDto updateEventForUser(long userId, UpdateEventRequest eventDto) {
        var user = userService.getUser(userId);
        var event = getEventByIdAndUser(eventDto.getEventId(), user);

        if (!event.getInitiator().equals(user)) {
            throw new UserIsNotEventInitiatorException(userId, event.getId());
        }
        if (event.getState() == EventState.PUBLISHED) {
            throw new UpdatePublishedEventException(event.getId());
        }

        checkEventDate(eventDto.getEventDate(), TIME_SHIFT_2_HOURS);

        if (eventDto.getPaid() != null) {
            event.setPaid(eventDto.getPaid());
        }
        if (eventDto.getAnnotation() != null) {
            event.setAnnotation(eventDto.getAnnotation());
        }
        if (eventDto.getDescription() != null) {
            event.setDescription(eventDto.getDescription());
        }
        if (eventDto.getTitle() != null) {
            event.setTitle(eventDto.getTitle());
        }
        if (eventDto.getCategory() != null) {
            event.setCategory(categoryService.getCategoryById(eventDto.getCategory()));
        }
        if (eventDto.getEventDate() != null) {
            event.setEventDate(eventDto.getEventDate());
        }
        if (eventDto.getParticipantLimit() != null) {
            event.setParticipantsLimit(eventDto.getParticipantLimit());
        }
        if (event.getState() == EventState.CANCELED) {
            event.setState(EventState.PENDING);
        }
        /*
        Редактировать можно только неопубликованные или отмененные события,
        а такие события не могут иметь просмотров и подтвержденных заявок на участие.
        Поэтому параметры confirmedRequests и views в mapToFullDto равны 0
         */
        return EventMapper.mapToFullDto(event, 0, 0);
    }

    @Override
    @Transactional
    public EventFullDto createNewEventForUser(long userId, NewEventDto eventDto) {
        var user = userService.getUser(userId);
        var category = categoryService.getCategoryById(eventDto.getCategory());

        checkEventDate(eventDto.getEventDate(), TIME_SHIFT_2_HOURS);

        var event = EventMapper.mapFromDto(eventDto, user, category);

        return EventMapper.mapToFullDto(eventRepository.save(event), 0, 0);
    }

    @Override
    public EventFullDto getEventForUser(long eventId, long userId) {
        var user = userService.getUser(userId);
        var event = getEventByIdAndUser(eventId, user);

        return EventMapper.mapToFullDto(event, requestService.getConfirmedRequest(event), getStatisticsByEvent(event));
    }

    @Override
    @Transactional
    public EventFullDto cancelEventForUser(long eventId, long userId) {
        var user = userService.getUser(userId);
        var event = getEventByIdAndUser(eventId, user);

        if (event.getState() == EventState.PUBLISHED) {
            throw new CancelingPublishedEventException();
        }
        if (event.getState() == EventState.PENDING) {
            event.setState(EventState.CANCELED);
        }
        return EventMapper.mapToFullDto(event, 0, 0);
    }

    @Override
    public List<EventFullDto> getAllEventsFilteredForAdmin(EventAdminFilterParam filterParam) {
        Specification<Event> constraints = null;

        if (filterParam.getUserIds() != null) {
            constraints = createOrAddConstraint(null, new SearchingByUsers(filterParam.getUserIds()));
        }
        if (filterParam.getCategoryIds() != null) {
            constraints = createOrAddConstraint(constraints, new SearchingByCategories(filterParam.getCategoryIds()));
        }
        if (filterParam.getStates() != null) {
            constraints = createOrAddConstraint(constraints, new SearchingByListOfStates(filterParam.getStates()));
        }
        if (filterParam.getRangeStart() != null) {
            constraints = createOrAddConstraint(constraints, new SearchingByRangeStart(filterParam.getRangeStart()));
        }
        if (filterParam.getRangeEnd() != null) {
            constraints = createOrAddConstraint(constraints, new SearchingByRangeEnd(filterParam.getRangeEnd()));
        }

        var pageable = new PageableByOffsetAndSize(filterParam.getFrom(), filterParam.getSize());
        var events = eventRepository.findAll(constraints, pageable).toList();

        return EventMapper.mapToFullDto(events, requestService.getConfirmedRequests(events),
                getStatisticsByEvents(events));
    }

    @Override
    @Transactional
    public EventFullDto updateEventForAdmin(long eventId, AdminUpdateEventRequest eventDto) {
        var event = getEventById(eventId);
        var category = categoryService.getCategoryById(eventDto.getCategory());

        if (eventDto.getTitle() != null) {
            event.setTitle(eventDto.getTitle());
        }
        if (eventDto.getAnnotation() != null) {
            event.setAnnotation(eventDto.getAnnotation());
        }
        if (eventDto.getDescription() != null) {
            event.setDescription(eventDto.getDescription());
        }
        if (eventDto.getCategory() != null) {
            event.setCategory(category);
        }
        if (eventDto.getEventDate() != null) {
            event.setEventDate(eventDto.getEventDate());
        }
        if (eventDto.getLocation() != null) {
            event.setLocation(LocationMapper.mapFromDto(eventDto.getLocation()));
        }
        if (eventDto.getPaid() != null) {
            event.setPaid(eventDto.getPaid());
        }
        if (eventDto.getParticipantLimit() != null) {
            event.setParticipantsLimit(eventDto.getParticipantLimit());
        }
        if (eventDto.getRequestModeration() != null) {
            event.setRequestModeration(eventDto.getRequestModeration());
        }
        return EventMapper.mapToFullDto(event, requestService.getConfirmedRequest(event), getStatisticsByEvent(event));
    }

    @Override
    @Transactional
    public EventFullDto publishEvent(long eventId) {
        var event = getEventById(eventId);
        var timeOfPublication = LocalDateTime.now();

        checkEventDate(event.getEventDate(), TIME_SHIFT_1_HOUR);
        if (event.getState() == EventState.CANCELED) {
            throw new PublishNotPendingEventException(event.getId());
        }
        if (event.getState() == EventState.PENDING) {
            event.setPublishedOn(timeOfPublication);
            event.setState(EventState.PUBLISHED);
        }
        return EventMapper.mapToFullDto(event, 0, 0);
    }

    @Override
    @Transactional
    public EventFullDto rejectEvent(long eventId) {
        var event = getEventById(eventId);

        if (event.getState() != EventState.PENDING) {
            throw new RejectNotPendingException(eventId);
        }
        event.setState(EventState.CANCELED);
        return EventMapper.mapToFullDto(event, 0, 0);
    }

    @Override
    public Event getEventById(long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
    }

    @Override
    public long getStatisticsByEvent(@NonNull Event event) {
        var stat = statisticsClient.getStatistics(List.of(EVENT_BASE_URI + "/" + event.getId()),
                event.getCreatedOn(), LocalDateTime.now().plusYears(SHIFT_500_YEARS));

        if (stat.isEmpty()) {
            return 0;
        }

        if (stat.size() > 1) {
            log.warn("getStatisticsByEvent: сервер статистики вернул данные более чем для 1 url");
            return 0;
        }
        return stat.get(0).getHits();
    }

    @Override
    public Map<Long, Long> getStatisticsByEvents(Collection<Event> events) {
        var baseUri = EVENT_BASE_URI + "/";

        var eventsUris = events.stream()
                .map(ev -> baseUri + ev.getId())
                .collect(Collectors.toList());

        if (eventsUris.isEmpty()) {
            return Map.of();
        }

        var eventsStat = statisticsClient.getStatistics(eventsUris, LocalDateTime.now()
                .minusYears(SHIFT_500_YEARS), LocalDateTime.now().plusYears(SHIFT_500_YEARS));

        return eventsStat.stream().collect(
                Collectors.toMap(
                        view -> {
                            var eventIdString = view.getUri().substring(baseUri.length());
                            return Long.parseLong(eventIdString);
                        },
                        ViewStats::getHits));
    }

    @Override
    public Event getEventByIdAndUser(long eventId, User user) {
        return eventRepository.findByIdAndInitiator(eventId, user)
                .orElseThrow(() -> new EventOfUserNotFoundException(eventId, user.getId()));
    }

    @Override
    public List<Event> getEventsById(Collection<Long> events) {
        return eventRepository.findAllById(events);
    }

    @Override
    public boolean isCategoryUsed(Category category) {
        return eventRepository.existsByCategory(category);
    }

    @Override
    public Event getPublishedEventById(long eventId) {
        return eventRepository.findByIdAndState(eventId, EventState.PUBLISHED)
                .orElseThrow(() -> new PublishedEventNotFoundException(eventId));
    }

    private void checkEventDate(LocalDateTime dateTime, long timeShift) {
        if (dateTime != null && dateTime.isBefore(LocalDateTime.now().plusHours(timeShift))) {
            throw new IncorrectTimeShiftException(timeShift);
        }
    }

    private Specification<Event> createOrAddConstraint(Specification<Event> result,
                                                       Specification<Event> newConstraint) {
        if (result == null) {
            return newConstraint;
        }
        return result.and(newConstraint);
    }
}
