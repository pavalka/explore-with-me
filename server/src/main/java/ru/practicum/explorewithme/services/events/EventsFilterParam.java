package ru.practicum.explorewithme.services.events;

import lombok.Builder;
import lombok.Getter;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class EventsFilterParam {
    private HttpServletRequest request;

    private String text;

    private List<Long> categories;

    private Boolean paid;

    private LocalDateTime rangeStart;

    private LocalDateTime rangeEnd;

    private Boolean onlyAvailable;

    private EventSorting sort;

    private long from;

    private int size;
}
