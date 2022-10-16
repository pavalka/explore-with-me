package ru.practicum.explorewithme.exceptions.events;

import ru.practicum.explorewithme.exceptions.OperationConditionViolationException;

public class IncorrectTimeShiftException extends OperationConditionViolationException {
    private static final String MSG = "До начала события с меньше %d часа(-ов)";

    public IncorrectTimeShiftException(long timeShift) {
        super(String.format(MSG, timeShift));
    }
}
