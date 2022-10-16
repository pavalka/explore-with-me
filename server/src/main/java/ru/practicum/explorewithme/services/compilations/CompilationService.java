package ru.practicum.explorewithme.services.compilations;

import ru.practicum.explorewithme.dto.compilations.CompilationDto;
import ru.practicum.explorewithme.dto.compilations.NewCompilationDto;

import java.util.List;

public interface CompilationService {
    List<CompilationDto> getAllCompilationsFiltered(Boolean pinned, long from, int size);

    CompilationDto getCompilation(long compilationId);

    CompilationDto createNewCompilation(NewCompilationDto compilationDto);

    void deleteCompilation(long compilationId);

    void deleteEventFromCompilation(long compilationId, long eventId);

    void addEventToCompilation(long compilationId, long eventId);

    void unpinCompilation(long compilationId);

    void pinCompilation(long compilationId);
}
