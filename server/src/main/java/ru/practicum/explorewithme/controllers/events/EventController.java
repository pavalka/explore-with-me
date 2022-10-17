package ru.practicum.explorewithme.controllers.events;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.dto.comments.CommentShortDto;
import ru.practicum.explorewithme.dto.events.EventFullDto;
import ru.practicum.explorewithme.dto.events.EventShortDto;
import ru.practicum.explorewithme.services.comments.CommentService;
import ru.practicum.explorewithme.services.events.EventService;
import ru.practicum.explorewithme.services.events.EventSorting;
import ru.practicum.explorewithme.services.events.EventsFilterParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/events")
@Validated
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;
    private final CommentService commentService;

    //-------------------------- Event endpoints -------------------------------------

    @GetMapping
    public Collection<EventShortDto> getEvents(@RequestParam(name = "text", required = false) String text,
                                              @RequestParam(name = "categories", required = false) List<Long> categories,
                                              @RequestParam(name = "paid", required = false) Boolean paid,
                                              @RequestParam(name = "rangeStart", required = false)
                                                    @DateTimeFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss")
                                                           LocalDateTime rangeStart,
                                              @RequestParam(name = "rangeEnd", required = false)
                                                    @DateTimeFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss")
                                                           LocalDateTime rangeEnd,
                                              @RequestParam(name = "onlyAvailable", defaultValue = "false") Boolean onlyAvailable,
                                              @RequestParam(name = "sort", required = false) EventSorting sort,
                                              @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero long from,
                                              @RequestParam(name = "size", defaultValue = "10") @Positive int size,
                                              HttpServletRequest servletRequest) {
        var filterParam = EventsFilterParam.builder()
                .text(text)
                .categories(categories)
                .paid(paid)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .onlyAvailable(onlyAvailable)
                .sort(sort)
                .from(from)
                .size(size)
                .request(servletRequest)
                .build();

        return eventService.getAllEventsFiltered(filterParam);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventById(@PathVariable(name = "eventId") long eventId, HttpServletRequest servletRequest) {
        return eventService.getEvent(eventId, servletRequest);
    }

    //-------------------------- Comment endpoints -------------------------------------

    @GetMapping("/{eventId}/comments")
    public Collection<CommentShortDto> getCommentsByEventId(@PathVariable(name = "eventId") long eventId,
                                                            @RequestParam(name = "from", defaultValue = "0")
                                                                @PositiveOrZero long from,
                                                            @RequestParam(name = "size", defaultValue = "10")
                                                                @Positive int size) {
        return commentService.getCommentsForEvent(eventId, from, size);
    }
}
