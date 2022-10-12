package ru.practicum.explorewithme.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.dto.statistics.EndpointHit;
import ru.practicum.explorewithme.dto.statistics.ViewStats;
import ru.practicum.explorewithme.services.statistics.StatisticsService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class HitController {
    private final StatisticsService statisticsService;

    @PostMapping("/hit")
    public void createNewHit(@RequestBody @Valid EndpointHit hitDto) {
        statisticsService.createNewHit(hitDto);
    }

    @GetMapping("/stats")
    public Collection<ViewStats> getStatsFiltered(@RequestParam(name = "start")
                                                      @DateTimeFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss")
                                                              LocalDateTime start,
                                                  @RequestParam(name = "end")
                                                      @DateTimeFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss")
                                                              LocalDateTime end,
                                                  @RequestParam(name = "uris", required = false) List<String> uris,
                                                  @RequestParam(name = "unique", defaultValue = "false")
                                                              Boolean unique) {
        return statisticsService.getStatisticsFiltered(start, end, uris, unique);
    }
}
