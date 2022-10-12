package ru.practicum.explorewithme.mappers.location;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.dto.events.Location;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocationMapper {
    public static Location mapToDto(ru.practicum.explorewithme.entities.events.Location point) {
        if (point == null) {
            return null;
        }
        return new Location(point.getLon(), point.getLat());
    }

    public static ru.practicum.explorewithme.entities.events.Location mapFromDto(Location location) {
        if (location == null) {
            return null;
        }

        return new ru.practicum.explorewithme.entities.events.Location(location.getLat(), location.getLon());
    }
}
