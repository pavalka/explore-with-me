package ru.practicum.explorewithme.dto.comments;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.explorewithme.entities.comments.CommentStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentShortDto {
    private long id;

    private String text;

    private long authorId;

    private long eventId;

    @JsonFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss")
    private LocalDateTime created;

    private CommentStatus status;
}
