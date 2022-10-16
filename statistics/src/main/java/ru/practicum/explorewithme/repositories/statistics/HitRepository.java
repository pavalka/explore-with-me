package ru.practicum.explorewithme.repositories.statistics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.explorewithme.entities.statistics.Hit;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface HitRepository extends JpaRepository<Hit, Long>, JpaSpecificationExecutor<Hit> {
    @Query("SELECT h.uri AS uri, h.app AS app, COUNT(DISTINCT h.ip) AS quantity FROM Hit AS h WHERE h.uri IN :uris " +
            "AND h.timestamp BETWEEN :start AND :end GROUP BY h.uri, h.app")
    List<HitQuantity> countHitsByUrisAndTimestampBetweenAndIpDistinct(@Param("uris") Collection<String> uris,
                                                                      @Param("start") LocalDateTime start,
                                                                      @Param("end") LocalDateTime end);

    @Query("SELECT h.uri AS uri, h.app AS app, COUNT(h.ip) AS quantity FROM Hit AS h WHERE h.uri IN :uris " +
            "AND h.timestamp BETWEEN :start AND :end GROUP BY h.uri, h.app")
    List<HitQuantity> countHitsByUrisAndTimestampBetween(@Param("uris") Collection<String> uris,
                                                         @Param("start") LocalDateTime start,
                                                         @Param("end") LocalDateTime end);

    @Query("SELECT h.uri AS uri, h.app AS app, COUNT(h.ip) AS quantity FROM Hit AS h WHERE h.timestamp " +
            "BETWEEN :start AND :end GROUP BY h.uri, h.app")
    List<HitQuantity> countHitsByTimestampBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT h.uri AS uri, h.app AS app, COUNT(DISTINCT h.ip) AS quantity FROM Hit AS h WHERE h.timestamp " +
            "BETWEEN :start AND :end GROUP BY h.uri, h.app")
    List<HitQuantity> countHitsByTimestampBetweenAndIpDistinct(@Param("start") LocalDateTime start,
                                                               @Param("end") LocalDateTime end);
}
