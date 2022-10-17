package ru.practicum.explorewithme.services.comments;

import ru.practicum.explorewithme.dto.comments.CommentFullDto;
import ru.practicum.explorewithme.dto.comments.CommentShortDto;
import ru.practicum.explorewithme.dto.comments.NewCommentDto;
import ru.practicum.explorewithme.dto.comments.UpdateCommentDto;
import ru.practicum.explorewithme.entities.comments.CommentStatus;

import java.util.List;

public interface CommentService {
    //--------------------- Private services ---------------------------------------

    CommentFullDto createNewComment(long userId, NewCommentDto commentDto);

    CommentFullDto updateComment(long userId, long commentId, UpdateCommentDto commentDto);

    void deleteCommentByUser(long userId, long commentId);

    List<CommentFullDto> getAllCommentsByUser(long userId, long from, int size);

    CommentFullDto getCommentByUserAndId(long userId, long commentId);

    //--------------------- Public services ----------------------------------------

    List<CommentShortDto> getCommentsForEvent(long eventId, long from, int size);

    CommentFullDto getComment(long commentId);

    //--------------------- Admin services -----------------------------------------

    void setCommentStatus(long commentId, CommentStatus status);

    List<CommentFullDto> getCommentsFiltered(CommentFilterParam filterParam);

    // Удаляет все комментарии, помеченные пользователями как DELETED
    void deleteAllComments();

    void deleteCommentByAdmin(long commentId);

    CommentFullDto getCommentForAdmin(long commentId);
}
