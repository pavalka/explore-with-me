package ru.practicum.explorewithme.exceptions.events;

import ru.practicum.explorewithme.exceptions.OperationConditionViolationException;

public class CancelingPublishedEventException extends OperationConditionViolationException {
    private static final String MSG = "Отмена опубликованного события";

    public CancelingPublishedEventException() {
        super(MSG);
    }
}
