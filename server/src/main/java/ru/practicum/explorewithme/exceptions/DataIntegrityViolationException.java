package ru.practicum.explorewithme.exceptions;

public class DataIntegrityViolationException extends RuntimeException {
    public DataIntegrityViolationException(String msg) {
        super(msg);
    }
}
