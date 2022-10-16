package ru.practicum.explorewithme.utils.errors;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorsReasons {
    public static final String ELEMENT_NOT_FOUND = "Требуемый объект не найден";
    public static final String BAD_REQUEST = "Запрос составлен неверно";
    public static final String DATA_INTEGRITY_VIOLATION = "Нарушение целостности данных";
    public static final String SERVER_ERROR = "Произошла ошибка";
    public static final String OPERATION_CONDITIONS_VIOLATION = "Нарушение условий выполнения операции";
}
