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

    private String annotation;

    private CategoryDto category;

    private boolean paid;

    @JsonFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss")
    private LocalDateTime eventDate;

    private UserShortDto initiator;

    private String description;

    private long participantLimit;

    private EventState state;

    @JsonFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss")
    private LocalDateTime createdOn;

    @JsonFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss")
    private LocalDateTime publishedOn;

    private Location location;

    private boolean requestModeration;

    private long confirmedRequests;

    private long views;
}
