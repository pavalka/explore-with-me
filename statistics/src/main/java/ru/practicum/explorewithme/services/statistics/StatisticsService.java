package ru.practicum.explorewithme.services.statistics;

import ru.practicum.explorewithme.dto.statistics.EndpointHit;
import ru.practicum.explorewithme.dto.statistics.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticsService {
    void createNewHit(EndpointHit hitDto);

    List<ViewStats> getStatisticsFiltered(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
