package ru.practicum.explorewithme.services.statistics;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class StatisticsFilterParam {
    private LocalDateTime start;

    private LocalDateTime end;

    private List<String> uris;

    private Boolean unique;
}
