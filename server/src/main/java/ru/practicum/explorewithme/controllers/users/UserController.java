package ru.practicum.explorewithme.controllers.users;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.dto.comments.CommentFullDto;
import ru.practicum.explorewithme.dto.comments.NewCommentDto;
import ru.practicum.explorewithme.dto.comments.UpdateCommentDto;
import ru.practicum.explorewithme.dto.events.EventFullDto;
import ru.practicum.explorewithme.dto.events.EventShortDto;
import ru.practicum.explorewithme.dto.events.NewEventDto;
import ru.practicum.explorewithme.dto.events.UpdateEventRequest;
import ru.practicum.explorewithme.dto.requests.ParticipationRequestDto;
import ru.practicum.explorewithme.services.comments.CommentService;
import ru.practicum.explorewithme.services.events.EventService;
import ru.practicum.explorewithme.services.requests.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequestMapping("/users")
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {
    private final RequestService requestService;
    private final EventService eventService;
    private final CommentService commentService;

    //-------------------------- Requests endpoint -------------------------------------

    @GetMapping("/{userId}/events/{eventId}/requests")
    public Collection<ParticipationRequestDto> getAllRequestsForUserEvent(@PathVariable(name = "userId") long userId,
                                                                   @PathVariable(name = "eventId") long eventId) {
        return requestService.getAllRequestsByUserAndEvent(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirmRequest(@PathVariable(name = "userId") long userId,
                                                  @PathVariable(name = "eventId") long eventId,
                                                  @PathVariable(name = "reqId") long requestId) {
        return requestService.confirmRequest(userId, eventId, requestId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto rejectRequest(@PathVariable(name = "userId") long userId,
                                                 @PathVariable(name = "eventId") long eventId,
                                                 @PathVariable(name = "reqId") long requestId) {
        return requestService.rejectRequest(userId, eventId, requestId);
    }

    @GetMapping("/{userId}/requests")
    public Collection<ParticipationRequestDto> getAllRequestsByUser(@PathVariable(name = "userId") long userId) {
        return requestService.getAllRequestsByUser(userId);
    }

    @PostMapping("/{userId}/requests")
    public ParticipationRequestDto createNewRequest(@PathVariable(name = "userId") long userId,
                                                    @RequestParam(name = "eventId") long eventId) {
        return requestService.createNewRequest(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable(name = "userId") long userId,
                                                 @PathVariable(name = "requestId") long requestId) {
        return requestService.cancelRequest(userId, requestId);
    }

    //-------------------------- Events endpoint -------------------------------------

    @GetMapping("/{userId}/events")
    public Collection<EventShortDto> getEventsByUser(@PathVariable(name = "userId") long userId,
                                                     @RequestParam(name = "from") @PositiveOrZero long from,
                                                     @RequestParam(name = "size") @Positive int size) {
        return eventService.getAllEventsForUser(userId, from, size);
    }

    @PatchMapping("/{userId}/events")
    public EventFullDto updateEvent(@PathVariable(name = "userId") long userId,
                                    @RequestBody @Valid UpdateEventRequest eventDto) {
        return  eventService.updateEventForUser(userId, eventDto);
    }

    @PostMapping("/{userId}/events")
    public EventFullDto createNewEvent(@PathVariable(name = "userId") long userId,
                                       @RequestBody @Valid NewEventDto eventDto) {
        return eventService.createNewEventForUser(userId, eventDto);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getEventByUser(@PathVariable(name = "userId") long userId,
                                       @PathVariable(name = "eventId") long eventId) {
        return eventService.getEventForUser(eventId, userId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto cancelEvent(@PathVariable(name = "userId") long userId,
                                    @PathVariable(name = "eventId") long eventId) {
        return eventService.cancelEventForUser(eventId, userId);
    }

    //-------------------------- Comment endpoints -------------------------------------

    @PostMapping("/{userId}/comments")
    public CommentFullDto createNewComment(@PathVariable(name = "userId") long userId,
                                           @RequestBody @Valid NewCommentDto commentDto) {
        return commentService.createNewComment(userId, commentDto);
    }

    @PatchMapping("/{userId}/comments/{comId}")
    public CommentFullDto updateComment(@PathVariable(name = "userId") long userId,
                                        @PathVariable(name = "comId") long commentId,
                                        @RequestBody @Valid UpdateCommentDto commentDto) {
        return commentService.updateComment(userId, commentId, commentDto);
    }

    @DeleteMapping("/{userId}/comments/{comId}")
    public void deleteComment(@PathVariable(name = "userId") long userId,
                                        @PathVariable(name = "comId") long commentId) {
        commentService.deleteCommentByUser(userId, commentId);
    }

    @GetMapping("/{userId}/comments")
    public Collection<CommentFullDto> getAllCommentsByUser(@PathVariable(name = "userId") long userId,
                                                           @RequestParam(name = "from", defaultValue = "0") long from,
                                                           @RequestParam(name = "size", defaultValue = "10") int size) {
        return commentService.getAllCommentsByUser(userId, from, size);
    }

    @GetMapping("/{userId}/comments/{comId}")
    public CommentFullDto getCommentByUserAndId(@PathVariable(name = "userId") long userId,
                                                @PathVariable(name = "comId") long commentId) {
        return commentService.getCommentByUserAndId(userId, commentId);
    }
}
