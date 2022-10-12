package ru.practicum.explorewithme.statisticsclient;

import ru.practicum.explorewithme.dto.statistics.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticsClient {
    void sendStatistics(String uri, String ip);

    List<ViewStats> getStatistics(List<String> uris, LocalDateTime rangeStart, LocalDateTime rangeEnd);
}
