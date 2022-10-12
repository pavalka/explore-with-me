package ru.practicum.explorewithme.repositories.comments;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import ru.practicum.explorewithme.entities.comments.Comment;
import ru.practicum.explorewithme.entities.comments.Comment_;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@RequiredArgsConstructor
public class SearchingByAuthor implements Specification<Comment> {
    private final List<Long> authorIds;

    @Override
    public Predicate toPredicate(Root<Comment> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return root.get(Comment_.author).in(authorIds);
    }
}
