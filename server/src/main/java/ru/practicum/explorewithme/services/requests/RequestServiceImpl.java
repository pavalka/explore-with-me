package ru.practicum.explorewithme.services.requests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.requests.ParticipationRequestDto;
import ru.practicum.explorewithme.entities.events.Event;
import ru.practicum.explorewithme.entities.events.EventState;
import ru.practicum.explorewithme.entities.requests.Request;
import ru.practicum.explorewithme.entities.requests.RequestQuantity;
import ru.practicum.explorewithme.entities.requests.RequestStatus;
import ru.practicum.explorewithme.entities.users.User;
import ru.practicum.explorewithme.exceptions.EventIsNotPublishedException;
import ru.practicum.explorewithme.exceptions.requests.RequestAlreadyExistsException;
import ru.practicum.explorewithme.exceptions.requests.RequestForEventNotFound;
import ru.practicum.explorewithme.exceptions.requests.RequestNotFoundException;
import ru.practicum.explorewithme.exceptions.requests.RequestStatusIsNotPending;
import ru.practicum.explorewithme.exceptions.requests.RequestsLimitException;
import ru.practicum.explorewithme.exceptions.requests.UserIsEventInitiatorException;
import ru.practicum.explorewithme.exceptions.requests.UserIsNotRequestCreatorException;
import ru.practicum.explorewithme.mappers.requests.RequestMapper;
import ru.practicum.explorewithme.repositories.requests.RequestRepository;
import ru.practicum.explorewithme.services.events.ThirdPartEventService;
import ru.practicum.explorewithme.services.users.ThirdPartUserService;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService, ThirdPartRequestService {
    private final RequestRepository requestRepository;
    private final ThirdPartUserService userService;
    private final ThirdPartEventService eventService;

    @Autowired
    public RequestServiceImpl(RequestRepository requestRepository, ThirdPartUserService userService,
                              @Lazy ThirdPartEventService eventService) {
        this.requestRepository = requestRepository;
        this.userService = userService;
        this.eventService = eventService;
    }

    @Override
    public List<ParticipationRequestDto> getAllRequestsByUser(long userId) {
        var user = userService.getUser(userId);

        return RequestMapper.mapToDto(requestRepository.findByUser(user));
    }

    @Override
    @Transactional
    public ParticipationRequestDto createNewRequest(long userId, long eventId) {
        var user = userService.getUser(userId);
        var event = eventService.getEventById(eventId);

        if (event.getState() != EventState.PUBLISHED) {
            throw new EventIsNotPublishedException(eventId);
        }
        if (event.getInitiator().equals(user)) {
            throw new UserIsEventInitiatorException(userId, eventId);
        }
        if (requestRepository.existsByUserAndEvent(user, event)) {
            throw new RequestAlreadyExistsException(userId, eventId);
        }
        if (!event.getAvailable()) {
            throw new RequestsLimitException(eventId);
        }

        Request request;

        if (!event.getRequestModeration()) {
            request = requestRepository.save(new Request(event, user, RequestStatus.CONFIRMED));
            checkAndMakeEventUnavailable(event);
        } else {
            request = requestRepository.save(new Request(event, user));
        }
        return RequestMapper.mapToDto(request);
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelRequest(long userId, long requestId) {
        var user = userService.getUser(userId);
        var request = getRequestById(requestId);
        var event = request.getEvent();

        if (request.getUser().equals(user)) {
            var prevStatus = request.getStatus();

            request.setStatus(RequestStatus.CANCELED);
            if (prevStatus == RequestStatus.CONFIRMED) {
                event.setAvailable(true);
            }
            return RequestMapper.mapToDto(request);
        }
        throw new UserIsNotRequestCreatorException(userId, requestId);
    }

    @Override
    public List<ParticipationRequestDto> getAllRequestsByUserAndEvent(long userId, long eventId) {
        var user = userService.getUser(userId);
        var event = eventService.getEventByIdAndUser(eventId, user);

        return RequestMapper.mapToDto(event.getRequests());
    }

    @Override
    @Transactional
    public ParticipationRequestDto confirmRequest(long userId, long eventId, long requestId) {
        var user = userService.getUser(userId);
        var request = getRequestById(requestId);
        var event = request.getEvent();

        if (!user.equals(event.getInitiator())) {
            throw new UserIsNotRequestCreatorException(userId, eventId);
        }
        if (event.getId() != eventId) {
            throw new RequestForEventNotFound(requestId, eventId);
        }
        if (!event.getAvailable()) {
            throw new RequestsLimitException(eventId);
        }
        if (request.getStatus() != RequestStatus.PENDING) {
            throw new RequestStatusIsNotPending(requestId);
        }
        request.setStatus(RequestStatus.CONFIRMED);
        if (checkAndMakeEventUnavailable(event)) {
            requestRepository.updateStatusByEventAndStatus(RequestStatus.REJECTED, event, RequestStatus.PENDING);
        }
        return RequestMapper.mapToDto(request);
    }

    @Override
    public ParticipationRequestDto rejectRequest(long userId, long eventId, long requestId) {
        var user = userService.getUser(userId);
        var request = getRequestById(requestId);
        var event = request.getEvent();

        if (!user.equals(event.getInitiator())) {
            throw new UserIsNotRequestCreatorException(userId, eventId);
        }
        if (event.getId() != eventId) {
            throw new RequestForEventNotFound(requestId, eventId);
        }
        if (request.getStatus() != RequestStatus.PENDING) {
            throw new RequestStatusIsNotPending(requestId);
        }
        request.setStatus(RequestStatus.REJECTED);
        return RequestMapper.mapToDto(request);
    }

    @Override
    public Map<Long, Long> getConfirmedRequests(Collection<Event> events) {
        var requestQuantities = requestRepository.countRequestsForEvents(events);

        return requestQuantities.stream()
                .collect(Collectors
                        .toMap(RequestQuantity::getRequestId, RequestQuantity::getRequestQuantity));
    }

    @Override
    public long getConfirmedRequest(Event event) {
        return requestRepository.countByEvent(event);
    }

    @Override
    public Request getRequestById(long requestId) {
        return requestRepository.findById(requestId).orElseThrow(() -> new RequestNotFoundException(requestId));
    }

    @Override
    public boolean isExistConfirmedRequestForUserAndEvent(User user, Event event) {
        return requestRepository.existsByUserAndEventAndStatus(user, event, RequestStatus.CONFIRMED);
    }

    private boolean checkAndMakeEventUnavailable(Event event) {
        if (event.getParticipantsLimit() > 0 && requestRepository.countByEventAndStatusIs(event,
                RequestStatus.CONFIRMED) >= event.getParticipantsLimit() - 1) {
            event.setAvailable(false);
            return true;
        }
        return false;
    }
}
