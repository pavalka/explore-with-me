package ru.practicum.explorewithme.dto.compilations;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class NewCompilationDto {
    @NotEmpty
    @Size(max = 150)
    private String title;

    private boolean pinned = false;

    private Set<Long> events;
}
