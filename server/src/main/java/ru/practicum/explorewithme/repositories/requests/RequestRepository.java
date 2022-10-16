package ru.practicum.explorewithme.repositories.requests;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.explorewithme.entities.events.Event;
import ru.practicum.explorewithme.entities.requests.Request;
import ru.practicum.explorewithme.entities.requests.RequestQuantity;
import ru.practicum.explorewithme.entities.requests.RequestStatus;
import ru.practicum.explorewithme.entities.users.User;

import java.util.Collection;
import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    @Query("SELECT r.event.id AS requestId, COUNT(r.id) AS requestQuantity " +
            "FROM Request AS r GROUP BY r.event.id HAVING r.event IN :events")
    List<RequestQuantity> countRequestsForEvents(@Param("events") Collection<Event> events);

    List<Request> findByUser(User user);

    boolean existsByUserAndEvent(User user, Event event);

    long countByEvent(Event event);

    long countByEventAndStatusIs(Event event, RequestStatus status);

    @Modifying
    @Query("update Request r set r.status = :newStatus where r.event = :event and r.status = :oldStatus")
    void updateStatusByEventAndStatus(@Param("newStatus") RequestStatus newStatus, @Param("event") Event event,
                                      @Param("oldStatus") RequestStatus requestStatus);

    boolean existsByUserAndEventAndStatus(User user, Event event, RequestStatus status);
}
