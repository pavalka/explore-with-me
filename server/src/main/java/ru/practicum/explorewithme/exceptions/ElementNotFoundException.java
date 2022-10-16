package ru.practicum.explorewithme.exceptions;

public class ElementNotFoundException extends RuntimeException {
    public ElementNotFoundException(String msg) {
        super(msg);
    }
}
