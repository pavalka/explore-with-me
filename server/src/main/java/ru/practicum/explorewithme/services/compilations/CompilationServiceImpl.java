package ru.practicum.explorewithme.services.compilations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.compilations.CompilationDto;
import ru.practicum.explorewithme.dto.compilations.NewCompilationDto;
import ru.practicum.explorewithme.entities.compilations.Compilation;
import ru.practicum.explorewithme.entities.events.Event;
import ru.practicum.explorewithme.exceptions.compilations.CompilationNotFoundException;
import ru.practicum.explorewithme.mappers.compilations.CompilationMapper;
import ru.practicum.explorewithme.pageable.PageableByOffsetAndSize;
import ru.practicum.explorewithme.repositories.compilations.CompilationRepository;
import ru.practicum.explorewithme.services.events.ThirdPartEventService;
import ru.practicum.explorewithme.services.requests.ThirdPartRequestService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final ThirdPartRequestService requestService;
    private final ThirdPartEventService eventService;

    @Override
    @Transactional(readOnly = true)
    public List<CompilationDto> getAllCompilationsFiltered(Boolean pinned, long from, int size) {
        var pageable = new PageableByOffsetAndSize(from, size);
        List<Compilation> compilations;

        if (pinned == null) {
            compilations = compilationRepository.findAll(pageable).toList();
        } else {
            compilations = compilationRepository.findByPinned(pinned, pageable);
        }

        var events = compilations.stream().flatMap(compilation -> compilation.getEvents().stream())
                .collect(Collectors.toSet());
        var confirmedRequests = requestService.getConfirmedRequests(events);
        var views = eventService.getStatisticsByEvents(events);

        return CompilationMapper.mapToDto(compilations, confirmedRequests, views);
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationDto getCompilation(long compilationId) {
        var compilation = getCompilationById(compilationId);
        var confirmedRequests = requestService.getConfirmedRequests(compilation.getEvents());
        var views = eventService.getStatisticsByEvents(compilation.getEvents());

        return CompilationMapper.mapToDto(compilation, confirmedRequests, views);
    }

    @Override
    public CompilationDto createNewCompilation(NewCompilationDto compilationDto) {
        List<Event> events = null;

        if (compilationDto.getEvents() != null) {
            events = eventService.getEventsById(compilationDto.getEvents());
        }

        var compilation = compilationRepository.save(CompilationMapper.mapFromDto(compilationDto, events));

        return CompilationMapper.mapToDto(compilation, Map.of(), Map.of());
    }

    @Override
    public void deleteCompilation(long compilationId) {
        var compilation = getCompilationById(compilationId);

        compilationRepository.delete(compilation);
    }

    @Override
    public void deleteEventFromCompilation(long compilationId, long eventId) {
        var compilation = getCompilationById(compilationId);
        var event = eventService.getEventById(eventId);
        var events = compilation.getEvents();

        events.remove(event);
    }

    @Override
    public void addEventToCompilation(long compilationId, long eventId) {
        var compilation = getCompilationById(compilationId);
        var event = eventService.getEventById(eventId);
        var events = compilation.getEvents();

        events.add(event);
    }

    @Override
    public void unpinCompilation(long compilationId) {
        var compilation = getCompilationById(compilationId);

        compilation.setPinned(false);
    }

    @Override
    public void pinCompilation(long compilationId) {
        var compilation = getCompilationById(compilationId);

        compilation.setPinned(true);
    }

    private Compilation getCompilationById(long compilationId) {
        return compilationRepository.findById(compilationId)
                .orElseThrow(() -> new CompilationNotFoundException(compilationId));
    }
}
