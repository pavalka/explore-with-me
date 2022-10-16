package ru.practicum.explorewithme.exceptions.categories;

import ru.practicum.explorewithme.exceptions.ElementNotFoundException;

public class CategoryNotFoundException extends ElementNotFoundException {
    private static final String msg = "Категория с id = %d не найдена";

    public CategoryNotFoundException(long categoryId) {
        super(String.format(msg, categoryId));
    }
}
