package ru.practicum.explorewithme.repositories.events;

import org.springframework.data.jpa.domain.Specification;
import ru.practicum.explorewithme.entities.events.Event;
import ru.practicum.explorewithme.entities.events.Event_;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class SearchingByAnnotationOrDescription implements Specification<Event> {
    private final String text;

    public SearchingByAnnotationOrDescription(String text) {
        this.text = String.format("%%%s%%", text.toLowerCase());
    }

    @Override
    public Predicate toPredicate(Root<Event> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(root.get(Event_.annotation)), text),
                criteriaBuilder.like(criteriaBuilder.lower(root.get(Event_.description)), text)
        );
    }
}
