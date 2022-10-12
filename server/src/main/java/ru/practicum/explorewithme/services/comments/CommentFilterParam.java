package ru.practicum.explorewithme.services.comments;

import lombok.Builder;
import lombok.Getter;
import ru.practicum.explorewithme.entities.comments.CommentStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class CommentFilterParam {
    private List<Long> eventIds;

    private List<Long> authorIds;

    private List<CommentStatus> statuses;

    private LocalDateTime rangeStart;

    private LocalDateTime rangeEnd;

    private Boolean edited;

    private long from;

    private int size;
}
