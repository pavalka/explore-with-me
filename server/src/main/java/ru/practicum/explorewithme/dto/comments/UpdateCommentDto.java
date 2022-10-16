package ru.practicum.explorewithme.dto.comments;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UpdateCommentDto {
    @NotEmpty
    @Size(max = 5000)
    private String text;
}
