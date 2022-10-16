package ru.practicum.explorewithme.services.statistics;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.statistics.EndpointHit;
import ru.practicum.explorewithme.dto.statistics.ViewStats;
import ru.practicum.explorewithme.mappers.statistics.StatisticsMapper;
import ru.practicum.explorewithme.repositories.statistics.HitQuantity;
import ru.practicum.explorewithme.repositories.statistics.HitRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {
    private final HitRepository hitRepository;

    @Override
    @Transactional
    public void createNewHit(EndpointHit hitDto) {
        var hit = StatisticsMapper.mapFromDto(hitDto);

        hitRepository.save(hit);
    }

    @Override
    public List<ViewStats> getStatisticsFiltered(LocalDateTime start, LocalDateTime end, List<String> uris,
                                                 Boolean unique) {
        List<HitQuantity> hitQuantities;

        if (unique) {
            if (uris != null && !uris.isEmpty()) {
                hitQuantities = hitRepository.countHitsByUrisAndTimestampBetweenAndIpDistinct(uris, start, end);
            } else {
                hitQuantities = hitRepository.countHitsByTimestampBetweenAndIpDistinct(start, end);
            }
        } else {
            if (uris != null && !uris.isEmpty()) {
                hitQuantities = hitRepository.countHitsByUrisAndTimestampBetween(uris, start, end);
            } else {
                hitQuantities = hitRepository.countHitsByTimestampBetween(start, end);
            }
        }
        return StatisticsMapper.mapToDto(hitQuantities);
    }
}
