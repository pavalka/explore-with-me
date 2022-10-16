package ru.practicum.explorewithme.exceptions.requests;

import ru.practicum.explorewithme.exceptions.OperationConditionViolationException;

public class RequestStatusIsNotPending extends OperationConditionViolationException {
    private static final String MSG = "Статус запроса на участие с id = %d отличен от PENDING";

    public RequestStatusIsNotPending(long requestId) {
        super(String.format(MSG, requestId));
    }
}
