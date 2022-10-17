package ru.practicum.explorewithme.mappers.requests;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.dto.requests.ParticipationRequestDto;
import ru.practicum.explorewithme.entities.requests.Request;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestMapper {
    public static ParticipationRequestDto mapToDto(Request request) {
        if (request == null) {
            return null;
        }

        return new ParticipationRequestDto(request.getId(), request.getEvent().getId(), request.getUser().getId(),
                request.getCreated(), request.getStatus());
    }

    public static List<ParticipationRequestDto> mapToDto(Collection<Request> requests) {
        if (requests == null) {
            return null;
        }
        return requests.stream().map(RequestMapper::mapToDto).collect(Collectors.toList());
    }
}
