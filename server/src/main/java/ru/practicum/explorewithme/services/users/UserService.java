package ru.practicum.explorewithme.services.users;

import ru.practicum.explorewithme.dto.users.NewUserRequest;
import ru.practicum.explorewithme.dto.users.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers(List<Long> usersIds);
    
    List<UserDto> getUsers(int from, int size);

    UserDto createUser(NewUserRequest user);

    void deleteUser(long userId);
}
