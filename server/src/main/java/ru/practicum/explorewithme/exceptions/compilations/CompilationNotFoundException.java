package ru.practicum.explorewithme.exceptions.compilations;

import ru.practicum.explorewithme.exceptions.ElementNotFoundException;

public class CompilationNotFoundException extends ElementNotFoundException {
    private static final String MSG = "Подборка событий с id = %d не найдена";

    public CompilationNotFoundException(long compilationId) {
        super(String.format(MSG, compilationId));
    }
}
