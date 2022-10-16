package ru.practicum.explorewithme.repositories.events;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import ru.practicum.explorewithme.entities.events.Event;
import ru.practicum.explorewithme.entities.events.EventState;
import ru.practicum.explorewithme.entities.events.Event_;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@RequiredArgsConstructor
public class SearchingByState implements Specification<Event> {
    private final EventState state;

    @Override
    public Predicate toPredicate(Root<Event> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.equal(root.get(Event_.state), state);
    }
}
