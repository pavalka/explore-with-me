package ru.practicum.explorewithme.dto.compilations;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.explorewithme.dto.events.EventShortDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDto {
    private long id;

    private String title;

    private boolean pinned;

    private List<EventShortDto> events;
}
