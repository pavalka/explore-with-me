package ru.practicum.explorewithme.mappers.compilations;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ru.practicum.explorewithme.dto.compilations.CompilationDto;
import ru.practicum.explorewithme.dto.compilations.NewCompilationDto;
import ru.practicum.explorewithme.entities.compilations.Compilation;
import ru.practicum.explorewithme.entities.events.Event;
import ru.practicum.explorewithme.mappers.events.EventMapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CompilationMapper {
    public static CompilationDto mapToDto(Compilation compilation, @NonNull Map<Long, Long> confirmedRequests,
                                          @NonNull Map<Long, Long> views) {
        if (compilation == null) {
            return null;
        }

        return new CompilationDto(
                compilation.getId(),
                compilation.getTitle(),
                compilation.getPinned(),
                EventMapper.mapToShortDto(compilation.getEvents(), confirmedRequests, views));
    }

    public static List<CompilationDto> mapToDto(Collection<Compilation> compilations,
                                                @NonNull Map<Long, Long> confirmedRequests,
                                                @NonNull Map<Long, Long> views) {
        if (compilations == null) {
            return null;
        }

        return compilations.stream().map(compilation -> mapToDto(compilation, confirmedRequests, views))
                .collect(Collectors.toList());
    }

    public static Compilation mapFromDto(NewCompilationDto compilationDto, List<Event> events) {
        if (compilationDto == null) {
            return null;
        }

        return new Compilation(compilationDto.getTitle(), compilationDto.isPinned(), events);
    }
}
