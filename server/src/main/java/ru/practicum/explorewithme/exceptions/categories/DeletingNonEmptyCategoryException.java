package ru.practicum.explorewithme.exceptions.categories;

import ru.practicum.explorewithme.exceptions.DataIntegrityViolationException;

public class DeletingNonEmptyCategoryException extends DataIntegrityViolationException {
    private static final String MSG = "Нельзя удалить непустую категорию. Категория с id = %d не пустая";

    public DeletingNonEmptyCategoryException(long categoryId) {
        super(String.format(MSG, categoryId));
    }
}
