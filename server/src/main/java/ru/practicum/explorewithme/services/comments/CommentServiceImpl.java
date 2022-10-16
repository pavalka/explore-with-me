package ru.practicum.explorewithme.services.comments;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.comments.CommentFullDto;
import ru.practicum.explorewithme.dto.comments.CommentShortDto;
import ru.practicum.explorewithme.dto.comments.NewCommentDto;
import ru.practicum.explorewithme.dto.comments.UpdateCommentDto;
import ru.practicum.explorewithme.entities.comments.Comment;
import ru.practicum.explorewithme.entities.comments.CommentStatus;
import ru.practicum.explorewithme.exceptions.comments.ApprovingNonPendingCommentException;
import ru.practicum.explorewithme.exceptions.comments.CommentOfUserNotFoundException;
import ru.practicum.explorewithme.exceptions.comments.InvalidCommentCreateTimeException;
import ru.practicum.explorewithme.exceptions.comments.CommentNotFoundException;
import ru.practicum.explorewithme.exceptions.comments.InvalidEventCreateTimeException;
import ru.practicum.explorewithme.exceptions.comments.UpdatingModeratedCommentException;
import ru.practicum.explorewithme.exceptions.comments.UserIsNotCommentAuthorException;
import ru.practicum.explorewithme.exceptions.comments.UserIsNotEventParticipantException;
import ru.practicum.explorewithme.mappers.comments.CommentMapper;
import ru.practicum.explorewithme.pageable.PageableByOffsetAndSize;
import ru.practicum.explorewithme.repositories.comments.CommentsRepository;
import ru.practicum.explorewithme.repositories.comments.SearchingByCreatedAndRangeEnd;
import ru.practicum.explorewithme.repositories.comments.SearchingByCreatedAndRangeStart;
import ru.practicum.explorewithme.repositories.comments.SearchingByCreatedBetweenRangeStartAndRangeEnd;
import ru.practicum.explorewithme.repositories.comments.SearchingByEditedAndRangeEnd;
import ru.practicum.explorewithme.repositories.comments.SearchingByEditedAndRangeStart;
import ru.practicum.explorewithme.repositories.comments.SearchingByEditedBetweenRangeStartAndRangeEnd;
import ru.practicum.explorewithme.repositories.comments.SearchingByEventIds;
import ru.practicum.explorewithme.repositories.comments.SearchingByStatus;
import ru.practicum.explorewithme.services.events.ThirdPartEventService;
import ru.practicum.explorewithme.services.requests.ThirdPartRequestService;
import ru.practicum.explorewithme.services.users.ThirdPartUserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommentServiceImpl implements CommentService {
    private static final long TIME_SHIFT = 1;

    private final CommentsRepository commentsRepository;
    private final ThirdPartUserService userService;
    private final ThirdPartEventService eventService;
    private final ThirdPartRequestService requestService;

    @Override
    @Transactional
    public CommentFullDto createNewComment(long userId, NewCommentDto commentDto) {
        var user = userService.getUser(userId);
        var event = eventService.getPublishedEventById(commentDto.getEventId());

        if (event.getEventDate().isAfter(LocalDateTime.now())) {
            throw new InvalidEventCreateTimeException(event.getId());
        }
        if (event.getEventDate().plusHours(TIME_SHIFT).isAfter(LocalDateTime.now())) {
            throw new InvalidCommentCreateTimeException(event.getId());
        }
        if (requestService.isExistConfirmedRequestForUserAndEvent(user, event)) {
            throw new UserIsNotEventParticipantException(userId, event.getId());
        }

        var comment = CommentMapper.mapFromDto(commentDto, user, event);

        return CommentMapper.mapToFullDto(commentsRepository.save(comment));
    }

    @Override
    @Transactional
    public CommentFullDto updateComment(long userId, long commentId, UpdateCommentDto commentDto) {
        var user = userService.getUser(userId);
        var comment = getNonDeletedCommentById(commentId);

        if (!comment.getAuthor().equals(user)) {
            throw new UserIsNotCommentAuthorException(userId, commentId);
        }
        if (comment.getStatus() != CommentStatus.PENDING) {
            throw new UpdatingModeratedCommentException(commentId);
        }

        comment.setText(commentDto.getText());
        comment.setEdited(LocalDateTime.now());
        return CommentMapper.mapToFullDto(comment);
    }

    @Override
    @Transactional
    public void deleteCommentByUser(long userId, long commentId) {
        var user = userService.getUser(userId);
        var comment = getNonDeletedCommentById(commentId);

        if (!comment.getAuthor().equals(user)) {
            throw new UserIsNotCommentAuthorException(userId, commentId);
        }
        comment.setStatus(CommentStatus.DELETED);
    }

    @Override
    public List<CommentFullDto> getAllCommentsByUser(long userId, long from, int size) {
        var user = userService.getUser(userId);
        var pageable = new PageableByOffsetAndSize(from, size);

        return CommentMapper.mapToFullDto(commentsRepository.findAllByAuthorAndStatusIsNot(user, CommentStatus.DELETED,
                pageable));
    }

    @Override
    public CommentFullDto getCommentByUserAndId(long userId, long commentId) {
        var comment = getNonDeletedCommentById(commentId);
        var user = userService.getUser(userId);

        if (comment.getAuthor().equals(user)) {
            throw new CommentOfUserNotFoundException(commentId, userId);
        }
        return CommentMapper.mapToFullDto(comment);
    }

    @Override
    public List<CommentShortDto> getCommentsForEvent(long eventId, long from, int size) {
        var event = eventService.getPublishedEventById(eventId);
        var pageable = new PageableByOffsetAndSize(from, size);

        return CommentMapper.mapToShortDto(commentsRepository.findByEventAndStatus(event, CommentStatus.MODERATED,
                pageable));
    }

    @Override
    public CommentFullDto getComment(long commentId) {
        return CommentMapper.mapToFullDto(commentsRepository.findByIdAndStatus(commentId, CommentStatus.MODERATED)
                .orElseThrow(() -> new CommentNotFoundException(commentId)));
    }

    @Override
    @Transactional
    public void setCommentStatus(long commentId, CommentStatus status) {
        var comment = getCommentById(commentId);

        if (comment.getStatus() != CommentStatus.PENDING) {
            throw new ApprovingNonPendingCommentException(commentId);
        }

        comment.setStatus(status);
        comment.setModerated(LocalDateTime.now());
    }

    @Override
    public List<CommentFullDto> getCommentsFiltered(CommentFilterParam filterParam) {
        var pageable = new PageableByOffsetAndSize(filterParam.getFrom(), filterParam.getSize());
        Specification<Comment> constraints = null;

        if (filterParam.getEventIds() != null) {
            constraints = createOrAddConstraint(null, new SearchingByEventIds(filterParam.getEventIds()));
        }
        if (filterParam.getAuthorIds() != null) {
            constraints = createOrAddConstraint(constraints, new SearchingByEventIds(filterParam.getAuthorIds()));
        }
        if (filterParam.getStatuses() != null) {
            constraints = createOrAddConstraint(constraints, new SearchingByStatus(filterParam.getStatuses()));
        }
        if (filterParam.getRangeStart() != null && filterParam.getRangeEnd() != null) {
            constraints = filterParam.getEdited() ? createOrAddConstraint(constraints,
                    new SearchingByEditedBetweenRangeStartAndRangeEnd(filterParam.getRangeStart(),
                            filterParam.getRangeEnd())) :
                    createOrAddConstraint(constraints,
                            new SearchingByCreatedBetweenRangeStartAndRangeEnd(filterParam.getRangeStart(),
                                    filterParam.getRangeEnd()));
        } else {
            if (filterParam.getRangeStart() != null) {
                constraints = filterParam.getEdited() ? createOrAddConstraint(constraints,
                        new SearchingByEditedAndRangeStart(filterParam.getRangeStart())) :
                        createOrAddConstraint(constraints, new SearchingByCreatedAndRangeStart(filterParam
                                .getRangeStart()));
            }
            if (filterParam.getRangeEnd() != null) {
                constraints = filterParam.getEdited() ? createOrAddConstraint(constraints,
                        new SearchingByEditedAndRangeEnd(filterParam.getRangeEnd())) :
                        createOrAddConstraint(constraints, new SearchingByCreatedAndRangeEnd(filterParam.getRangeEnd()));
            }
        }
        return CommentMapper.mapToFullDto(commentsRepository.findAll(constraints, pageable).toList());
    }

    @Override
    @Transactional
    public void deleteAllComments() {
        commentsRepository.deleteByStatus(CommentStatus.DELETED);
    }

    @Override
    @Transactional
    public void deleteCommentByAdmin(long commentId) {
        var comment = getCommentById(commentId);

        commentsRepository.delete(comment);
    }

    @Override
    public CommentFullDto getCommentForAdmin(long commentId) {
        return CommentMapper.mapToFullDto(getCommentById(commentId));
    }

    private Comment getCommentById(long commentId) {
        return commentsRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException(commentId));
    }

    private Comment getNonDeletedCommentById(long commentId) {
        return commentsRepository.findByIdAndStatusIsNot(commentId, CommentStatus.DELETED)
                .orElseThrow(() -> new CommentNotFoundException(commentId));
    }

    private Specification<Comment> createOrAddConstraint(Specification<Comment> result,
                                                       Specification<Comment> newConstraint) {
        if (result == null) {
            return newConstraint;
        }
        return result.and(newConstraint);
    }
}
