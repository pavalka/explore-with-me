package ru.practicum.explorewithme.exceptions;

public class OperationConditionViolationException extends RuntimeException {
    public OperationConditionViolationException(String msg) {
        super(msg);
    }
}
