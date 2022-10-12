package ru.practicum.explorewithme.dto.events;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.explorewithme.dto.categories.CategoryDto;
import ru.practicum.explorewithme.dto.users.UserShortDto;
import ru.practicum.explorewithme.entities.events.EventState;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class EventFullDto {
    private long id;

    private String title;

    private String description;

    private String annotation;

    private CategoryDto category;

    private UserShortDto initiator;

    @JsonFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss")
    private LocalDateTime createdOn;

    @JsonFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss")
    private LocalDateTime eventDate;

    @JsonFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss")
    private LocalDateTime publishedOn;

    private Location location;

    private boolean paid;

    private boolean requestModeration;

    private EventState state;

    private long participantLimit;

    private long confirmedRequests;

    private long views;
}
