package ru.practicum.explorewithme.controllers.comments;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.dto.comments.CommentFullDto;
import ru.practicum.explorewithme.services.comments.CommentService;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/{comId}")
    public CommentFullDto getCommentById(@PathVariable(name = "comId") long commentId) {
        return commentService.getComment(commentId);
    }
}
