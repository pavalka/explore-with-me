package ru.practicum.explorewithme.services.events;

import lombok.Builder;
import lombok.Getter;
import ru.practicum.explorewithme.entities.events.EventState;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class EventAdminFilterParam {
    private List<Long> userIds;

    private List<EventState> states;

    private List<Long> categoryIds;

    private LocalDateTime rangeStart;

    private LocalDateTime rangeEnd;

    private long from;

    private int size;
}
