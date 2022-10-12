package ru.practicum.explorewithme.dto.events;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.explorewithme.dto.categories.CategoryDto;
import ru.practicum.explorewithme.dto.users.UserShortDto;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class EventShortDto {
    private long id;

    private String title;

    private String annotation;

    private CategoryDto category;

    private UserShortDto initiator;

    @JsonFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss")
    private LocalDateTime eventDate;

    private boolean paid;

    private long confirmedRequests;

    private long views;
}
