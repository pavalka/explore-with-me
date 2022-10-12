package ru.practicum.explorewithme.mappers.comments;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ru.practicum.explorewithme.dto.comments.CommentFullDto;
import ru.practicum.explorewithme.dto.comments.CommentShortDto;
import ru.practicum.explorewithme.dto.comments.NewCommentDto;
import ru.practicum.explorewithme.entities.comments.Comment;
import ru.practicum.explorewithme.entities.events.Event;
import ru.practicum.explorewithme.entities.users.User;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentMapper {
    public static CommentFullDto mapToFullDto(Comment comment) {
        if (comment == null) {
            return null;
        }

        var commentDto = new CommentFullDto();

        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setAuthorId(comment.getAuthor().getId());
        commentDto.setEventId(comment.getEvent().getId());
        commentDto.setCreated(comment.getCreated());
        commentDto.setModerated(comment.getModerated());
        commentDto.setEdited(comment.getEdited());
        commentDto.setStatus(comment.getStatus());
        return commentDto;
    }

    public static CommentShortDto mapToShortDto(Comment comment) {
        if (comment == null) {
            return null;
        }

        var commentDto = new CommentShortDto();

        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setAuthorId(comment.getAuthor().getId());
        commentDto.setEventId(comment.getEvent().getId());
        commentDto.setCreated(comment.getCreated());
        commentDto.setStatus(comment.getStatus());
        return commentDto;
    }

    public static List<CommentShortDto> mapToShortDto(Collection<Comment> comments) {
        if (comments == null) {
            return null;
        }

        return comments.stream().map(CommentMapper::mapToShortDto).collect(Collectors.toList());
    }

    public static List<CommentFullDto> mapToFullDto(Collection<Comment> comments) {
        if (comments == null) {
            return null;
        }

        return comments.stream().map(CommentMapper::mapToFullDto).collect(Collectors.toList());
    }

    public static Comment mapFromDto(NewCommentDto commentDto, @NonNull User user, @NonNull Event event) {
        if (commentDto == null) {
            return null;
        }

        return new Comment(commentDto.getText(), user, event);
    }
}
