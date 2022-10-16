package ru.practicum.explorewithme.repositories.comments;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import ru.practicum.explorewithme.entities.comments.Comment;
import ru.practicum.explorewithme.entities.comments.Comment_;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;

@RequiredArgsConstructor
public class SearchingByEditedBetweenRangeStartAndRangeEnd implements Specification<Comment> {
    private final LocalDateTime start;
    private final LocalDateTime end;

    @Override
    public Predicate toPredicate(Root<Comment> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.between(root.get(Comment_.edited), start, end);
    }
}
