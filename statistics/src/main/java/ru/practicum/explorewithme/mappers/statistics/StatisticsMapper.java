package ru.practicum.explorewithme.mappers.statistics;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.dto.statistics.EndpointHit;
import ru.practicum.explorewithme.dto.statistics.ViewStats;
import ru.practicum.explorewithme.entities.statistics.Hit;
import ru.practicum.explorewithme.repositories.statistics.HitQuantity;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StatisticsMapper {
    public static ViewStats mapToDto(HitQuantity hitQuantity) {
        if (hitQuantity == null) {
            return null;
        }
        return new ViewStats(hitQuantity.getApp(), hitQuantity.getUri(), hitQuantity.getQuantity());
    }

    public static List<ViewStats> mapToDto(Collection<HitQuantity> hits) {
        if (hits == null) {
            return null;
        }
        return hits.stream().map(StatisticsMapper::mapToDto).collect(Collectors.toList());
    }

    public static Hit mapFromDto(EndpointHit hitDto) {
        if (hitDto == null) {
            return null;
        }
        return new Hit(hitDto.getUri(), hitDto.getTimestamp(), hitDto.getApp(), hitDto.getIp());
    }
}
