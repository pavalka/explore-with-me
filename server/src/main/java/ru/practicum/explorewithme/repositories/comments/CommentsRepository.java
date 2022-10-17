package ru.practicum.explorewithme.repositories.comments;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.practicum.explorewithme.entities.comments.Comment;
import ru.practicum.explorewithme.entities.comments.CommentStatus;
import ru.practicum.explorewithme.entities.events.Event;
import ru.practicum.explorewithme.entities.users.User;

import java.util.List;
import java.util.Optional;

public interface CommentsRepository extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {
    Optional<Comment> findByIdAndStatusIsNot(Long id, CommentStatus status);

    List<Comment> findByEventAndStatus(Event event, CommentStatus status, Pageable pageable);

    Optional<Comment> findByIdAndStatus(Long id, CommentStatus status);

    long deleteByStatus(CommentStatus status);

    List<Comment> findAllByAuthorAndStatusIsNot(User author, CommentStatus status, Pageable pageable);
}
