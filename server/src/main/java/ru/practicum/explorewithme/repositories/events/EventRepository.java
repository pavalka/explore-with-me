package ru.practicum.explorewithme.repositories.events;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.practicum.explorewithme.entities.categories.Category;
import ru.practicum.explorewithme.entities.events.Event;
import ru.practicum.explorewithme.entities.events.EventState;
import ru.practicum.explorewithme.entities.users.User;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
    List<Event> findByInitiator(User initiator, Pageable pageable);

    Optional<Event> findByIdAndInitiator(Long id, User initiator);

    boolean existsByCategory(Category category);

    Optional<Event> findByIdAndState(Long id, EventState state);
}
