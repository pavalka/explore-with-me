package ru.practicum.explorewithme.controllers.compilations;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.dto.compilations.CompilationDto;
import ru.practicum.explorewithme.services.compilations.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequestMapping("/compilations")
@Validated
@RequiredArgsConstructor
public class CompilationController {
    private final CompilationService compilationService;

    @GetMapping
    public Collection<CompilationDto> getAllCompilationsFiltered(@RequestParam(name = "pinned", required = false)
                                                                             Boolean pinned,
                                                                 @RequestParam(name = "from", defaultValue = "0")
                                                                    @PositiveOrZero long from,
                                                                 @RequestParam(name = "size", defaultValue = "10")
                                                                     @Positive int size) {
        return compilationService.getAllCompilationsFiltered(pinned, from, size);
    }

    @GetMapping("/{compilationId}")
    public CompilationDto getCompilationById(@PathVariable(name = "compilationId") long compilationId) {
        return compilationService.getCompilation(compilationId);
    }
}
