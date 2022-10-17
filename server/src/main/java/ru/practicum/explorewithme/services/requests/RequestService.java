package ru.practicum.explorewithme.services.requests;

import ru.practicum.explorewithme.dto.requests.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    List<ParticipationRequestDto> getAllRequestsByUser(long userId);

    ParticipationRequestDto createNewRequest(long userId, long eventId);

    ParticipationRequestDto cancelRequest(long userId, long requestId);

    List<ParticipationRequestDto> getAllRequestsByUserAndEvent(long userId, long eventId);

    ParticipationRequestDto confirmRequest(long userId, long eventId, long requestId);

    ParticipationRequestDto rejectRequest(long userId, long eventId, long requestId);
}
