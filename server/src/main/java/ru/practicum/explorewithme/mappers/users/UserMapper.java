package ru.practicum.explorewithme.mappers.users;

import ru.practicum.explorewithme.dto.users.NewUserRequest;
import ru.practicum.explorewithme.dto.users.UserDto;
import ru.practicum.explorewithme.dto.users.UserShortDto;
import ru.practicum.explorewithme.entities.users.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {
    public static UserDto mapToFullDto(User user) {
        if (user == null) {
            return null;
        }
        return new UserDto(user.getId(), user.getEmail(), user.getName());
    }

    public static List<UserDto> mapToFullDto(Collection<User> users) {
        if (users == null) {
            return null;
        }
        return users.stream().map(UserMapper::mapToFullDto).collect(Collectors.toList());
    }

    public static UserShortDto mapToShortDto(User user) {
        if (user == null) {
            return null;
        }
        return new UserShortDto(user.getId(), user.getName());
    }

    public static User mapFromDto(NewUserRequest userDto) {
        if (userDto == null) {
            return null;
        }
        return new User(userDto.getEmail(), userDto.getName());
    }
}
