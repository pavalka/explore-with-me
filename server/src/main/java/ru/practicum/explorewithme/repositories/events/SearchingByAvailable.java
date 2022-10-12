package ru.practicum.explorewithme.repositories.events;

import org.springframework.data.jpa.domain.Specification;
import ru.practicum.explorewithme.entities.events.Event;
import ru.practicum.explorewithme.entities.events.Event_;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class SearchingByAvailable implements Specification<Event> {
    @Override
    public Predicate toPredicate(Root<Event> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.equal(root.get(Event_.available), true);
    }
}
