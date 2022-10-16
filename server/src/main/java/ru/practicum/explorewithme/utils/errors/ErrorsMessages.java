package ru.practicum.explorewithme.utils.errors;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorsMessages {
    public static final String UNKNOWN_ERROR = "Неизвестная ошибка";
    public static final String REQUEST_PARAM_ERROR = "В запросе отсутствует один/несколько параметров";
    public static final String WRONG_REQUEST_BODY_VALUE_ERROR = "В теле запроса отсутствует одно/несколько полей или " +
            "имеют неверное значение";
    public static final String WRONG_REQUEST_PARAM_VALUE_ERROR = "Один/несколько параметров запроса имеют неверное" +
            " значение";
    public static final String DATA_INTEGRITY_VIOLATION_ERROR = "Нарушение целостности данных БД, например, нарушено" +
            " условие уникальности значений отдельных полей";
    public static final String INTERNAL_SERVER_ERROR = "Произошла внутрення ошибка сервера. Попробуйте повторить " +
            "операцию позже";
}
