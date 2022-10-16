package ru.practicum.explorewithme.dto.events;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class NewEventDto {
    @NotNull
    @Size(min = 3, max = 120)
    private String title;

    @NotNull
    @Size(min = 20, max = 2000)
    private String annotation;

    @NotNull
    @Size(min = 20, max = 7000)
    private String description;

    @NotNull
    private Long category;

    @NotNull
    private Location location;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss")
    private LocalDateTime eventDate;

    private boolean paid = false;

    @PositiveOrZero
    private int participantLimit = 0;

    private boolean requestModeration = true;
}
