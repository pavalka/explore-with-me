package ru.practicum.explorewithme.repositories.events;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import ru.practicum.explorewithme.entities.events.Event;
import ru.practicum.explorewithme.entities.events.Event_;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@RequiredArgsConstructor
public class SearchingByUsers implements Specification<Event> {
    private final List<Long> userIds;

    @Override
    public Predicate toPredicate(Root<Event> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return root.get(Event_.initiator).in(userIds);
    }
}
